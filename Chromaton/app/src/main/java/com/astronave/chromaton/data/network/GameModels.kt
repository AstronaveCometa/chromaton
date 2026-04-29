package com.astronave.chromaton.data.network

// Modelos para Usuarios
data class UserRequest(
    val name: String,
    val email: String
)
data class UserResponse(
    val id: String,
    val name: String,
    val email: String
)

// Modelos para Juegos
data class CreateGameRequest(
    val game_password: String
)

data class JoinGameRequest(
    val game_id: Int,
    val game_password: String
)

data class GameResponse(
    val game_id: Int,
    val host_id: String,
    val status: String
)

data class Player(
    val player_id: Int,
    val name: String
)

data class Game(
    val game_id: Int,
    val status: String
)

data class Dice(
    val id: Int,
    val color: String,
    val value: Int?, // Es opcional porque al inicio pueden no estar lanzados
    val position: Int?
)

data class GameDetailResponse(
    val id: Int,
    val status: String,
    val currentPlayerId: String?,
    val dices: List<Dice>,
    val players: List<Player>
)