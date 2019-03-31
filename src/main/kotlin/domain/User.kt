package domain

data class User(val name: String){

    val id = User.id++

    companion object {
        private var id: Long = 0L
    }
}
