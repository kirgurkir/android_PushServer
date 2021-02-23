package ru.netology.AndroidNotificationServer

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.gson.Gson
import java.io.FileInputStream


val pushType = PushType.NEW_POST
const val token =
    "<here token>"

fun main() {
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(FileInputStream("fcm.json")))
        //.setDatabaseUrl(dbUrl)
        .build()
    val gson = Gson()

    val like = Like(
        userId = 1,
        userName = "Kirill",
        postId = 2,
        postAuthor = "Netology"
    )

    val post = Post(
        id = 1,
        author = "Ivan",
        content = "Вернемся в прошлое — 20 век, кризисные эпохи. Кино отражало эти проблемы, пройденные человечеством, и отражает сегодняшнее состояние мира.\n" +
                "\n" +
                "Противостояние характеров, взрывы человеческих страстей, «спрессованные конфликты» — всё это занимает режиссёров. Помочь человеку осознать истинную реальность условий своего существования, вернуть ему утерянные чувства, освободить его от иллюзии — вот задачи авторского кино. При этом одним из важных аспектов является жёсткая критика современности.\n" +
                "\n" +
                "Кинематограф выступает как отражение действительности, а затем фиксирует результаты этого отражения, которые соотносятся как познание и язык.",
        published = "now",
        like = 9,
        likedByMe = false,
        share = 1,
        videoUrl = null,
        view = 100
    )

    FirebaseApp.initializeApp(options)

    val message = when (pushType) {
        PushType.LIKE -> Message.builder()
            .putData("action", PushType.LIKE.toString())
            .putData("content", gson.toJson(like))
        PushType.NEW_POST -> Message.builder()
            .putData("action", PushType.NEW_POST.toString())
            .putData("content", gson.toJson(post))
    }.setToken(token).build()
        ?: throw Exception()

    FirebaseMessaging.getInstance().send(message)
}

enum class PushType {
    LIKE,
    NEW_POST
}

data class Post(
    val id: Int,
    val author: String,
    val content: String,
    val published: String,
    val like: Int,
    val likedByMe: Boolean = false,
    val share: Int,
    val view: Int,
    val videoUrl: String?
)

data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,
)