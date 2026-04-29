package com.astronave.chromaton.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.GET

interface ApiService {

    @POST("users")
    suspend fun registerUser(
        @Header("Authorization") token: String,
        @Body request: UserRequest
    ): Response<UserResponse>

    @POST("games/create")
    suspend fun createGame(
        @Header("Authorization") token: String,
        @Body request: CreateGameRequest // Sin la "s" al final
    ): Response<GameResponse>

    @POST("games/join")
    suspend fun joinGame(
        @Header("Authorization") token: String,
        @Body request: JoinGameRequest
    ): Response<GameResponse>

    @GET("players/game/{game_id}")
    suspend fun getPlayersByGameId(
        @Path("game_id") gameId: Int
    ): Response<List<Player>>

    @POST("games/{game_id}/start")
    suspend fun startGame(
        @Path("game_id") gameId: Int
    ): Response<Map<String, Any>>

    @GET("games/{game_id}")
    suspend fun getGameById(
        @Path("game_id") gameId: Int
    ): Response<Game>

    @POST("games/{game_id}/start")
    suspend fun startGame(
        @Header("Authorization") token: String, // Agregamos el Token aquí
        @Path("game_id") gameId: Int
    ): Response<Map<String, Any>>

    @GET("games/{game_id}")
    suspend fun getGameById(
        @Header("Authorization") token: String, // El GET también está protegido en tus rutas
        @Path("game_id") gameId: Int
    ): Response<Game>

    @GET("games/{game_id}/details")
    suspend fun getGameDetails(
        @Header("Authorization") token: String,
        @Path("game_id") gameId: Int
    ): Response<GameDetailResponse>

    @POST("games/{game_id}/move")
    suspend fun makeMove(
        @Header("Authorization") token: String,
        @Path("game_id") gameId: Int,
        @Body moveData: MoveRequest
    ): Response<Unit>
}