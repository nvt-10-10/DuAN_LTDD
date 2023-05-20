package Model

class Answer(
    val id: Int,
    val content: String,
    val isCorrect: Int,
    val questionId: Int,
    val explain: String
)