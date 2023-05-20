package Model

class Question(
    val id: Int,
    val content: String,
    val imageLink: String,
    val list_answer: ArrayList<Answer>
)