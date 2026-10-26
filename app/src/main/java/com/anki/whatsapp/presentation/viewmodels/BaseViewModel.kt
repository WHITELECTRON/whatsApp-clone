package com.anki.whatsapp.presentation.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import com.anki.whatsapp.models.Message
import com.anki.whatsapp.presentation.chat_box.ChatListModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor() : ViewModel() {

    private val databaseReference = FirebaseDatabase.getInstance().reference

    private val _chatList = MutableStateFlow<List<ChatListModel>>(emptyList())
    val chatList = _chatList.asStateFlow()

    init {
        loadChatData()
    }

    fun searchUserByPhoneNumber(
        phoneNumber: String,
        callBack: (ChatListModel?) -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Log.d("BaseViewModel", "User is not authenticated")
            callBack(null)
            return
        }

        val database = FirebaseDatabase.getInstance().getReference("users")
        database.orderByChild("phoneNumber").equalTo(phoneNumber)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.children.firstOrNull()?.getValue(ChatListModel::class.java)
                        callBack(user)
                    } else {
                        callBack(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("BaseViewModel", "Error fetching user: ${error.message}, details: ${error.details}")
                    callBack(null)
                }
            })
    }

    fun getChatForUser(
        userId: String,
        callBack: (List<ChatListModel>) -> Unit
    ) {
        val chatRef = FirebaseDatabase.getInstance().getReference("users/$userId/chats")
        chatRef.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chatList = mutableListOf<ChatListModel>()
                    for (childSnapshot in snapshot.children) {
                        val chat = childSnapshot.getValue(ChatListModel::class.java)
                        if (chat != null) {
                            chatList.add(chat)
                        }
                    }
                    callBack(chatList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("BaseViewModel", "Error fetching user chat: ${error.message}")
                    callBack(emptyList())
                }
            })
    }

    private fun loadChatData() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            val chatRef = FirebaseDatabase.getInstance().getReference("chats")
            chatRef.orderByChild("userId").equalTo(currentUserId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val chatList = mutableListOf<ChatListModel>()
                        for (childSnapshot in snapshot.children) {
                            val chat = childSnapshot.getValue(ChatListModel::class.java)
                            if (chat != null) {
                                chatList.add(chat)
                            }
                        }
                        _chatList.value = chatList
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("BaseViewModel", "Error fetching chat data: ${error.message}")
                    }
                })
        }
    }

    fun addChat(newChat: ChatListModel) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            val newChatRef = FirebaseDatabase.getInstance().getReference("chats").push()
            val chatWithUser = newChat.copy(userId = currentUserId)

            newChatRef.setValue(chatWithUser)
                .addOnSuccessListener {
                    Log.d("BaseViewModel", "Chat added successfully to Firebase")
                }
                .addOnFailureListener { exception ->
                    Log.e("BaseViewModel", "Failed to add chat: ${exception.message}")
                }
        } else {
            Log.e("BaseViewModel", "No user is authenticated")
        }
    }

    fun sendMessage(
        senderPhoneNumber: String,
        receiverPhoneNumber: String,
        messageText: String
    ) {
        val messageId = databaseReference.push().key ?: return

        val message = Message(
            senderPhoneNumber = senderPhoneNumber,
            message = messageText,
            timeStamp = System.currentTimeMillis()
        )

        databaseReference.child("messages")
            .child(senderPhoneNumber)
            .child(receiverPhoneNumber)
            .child(messageId)
            .setValue(message)

        databaseReference.child("messages")
            .child(receiverPhoneNumber)
            .child(senderPhoneNumber)
            .child(messageId)
            .setValue(message)
    }

    fun getMessage(
        senderPhoneNumber: String,
        receiverPhoneNumber: String,
        onNewMessage: (Message) -> Unit
    ) {
        val messageRef = databaseReference.child("messages")
            .child(senderPhoneNumber)
            .child(receiverPhoneNumber)

        messageRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    onNewMessage(message)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun fetchLastMessageForChat(
        senderPhoneNumber: String,
        receiverPhoneNumber: String,
        onLastMessageFetched: (String, String) -> Unit
    ) {
        val chatRef = FirebaseDatabase.getInstance().reference
            .child("messages")
            .child(senderPhoneNumber)
            .child(receiverPhoneNumber)

        chatRef.orderByChild("timeStamp").limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val lastChild = snapshot.children.firstOrNull()
                        val lastMessage = lastChild?.child("message")?.getValue(String::class.java) ?: "No message"
                        val timeStamp = lastChild?.child("timeStamp")?.getValue(Long::class.java)?.toString() ?: "--:--"
                        onLastMessageFetched(lastMessage, timeStamp)
                    } else {
                        onLastMessageFetched("No message", "--:--")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("BaseViewModel", "Error fetching last message: ${error.message}")
                    onLastMessageFetched("No message", "--:--")
                }
            })
    }

    fun loadChatList(
        currentUserPhoneNumber: String,
        onChatListLoaded: (List<ChatListModel>) -> Unit
    ) {
        val chatList = mutableListOf<ChatListModel>()
        val chatRef = FirebaseDatabase.getInstance().reference
            .child("chats")
            .child(currentUserPhoneNumber)

        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val name = child.child("name").getValue(String::class.java) ?: "Unknown"
                        val image = child.child("image").getValue(String::class.java)

                        val otherUserPhoneNumber = child.key ?: ""
                        fetchLastMessageForChat(
                            senderPhoneNumber = currentUserPhoneNumber,
                            receiverPhoneNumber = otherUserPhoneNumber
                        ) { lastMessage, time ->
                            chatList.add(
                                ChatListModel(
                                    name = name,
                                    image = image,
                                    message = lastMessage,
                                    time = time
                                )
                            )
                            if (chatList.size == snapshot.childrenCount.toInt()) {
                                onChatListLoaded(chatList)
                            }
                        }
                    }
                } else {
                    onChatListLoaded(emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onChatListLoaded(emptyList())
            }
        })
    }

    private fun decodeBase64ToBitmap(base64Image: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IOException) {
            null
        }
    }

    fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val inputStream = ByteArrayInputStream(decodedBytes)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }
}
