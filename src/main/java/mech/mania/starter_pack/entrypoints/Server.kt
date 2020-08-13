package mech.mania.starter_pack.entrypoints

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import mech.mania.engine.domain.game.GameState
import mech.mania.engine.domain.game.characters.CharacterDecision
import mech.mania.engine.domain.model.PlayerProtos.*
import mech.mania.starter_pack.domain.PlayerStrategy
import mech.mania.starter_pack.domain.Strategy
import java.net.InetSocketAddress
import java.util.logging.Logger


/**
 * main function for running the server with no onReceive and onSend
 */
fun main(args: Array<String>) {
    Server().startServer(args[0].toInt(), {}, {})
}

class Server {

    private val logger = Logger.getLogger(Server::class.toString())
    private val player: Strategy = PlayerStrategy()


    /**
     * Starts a server using a specified port
     * TODO: allow URL instead of port
     * @param port Port to start server on (localhost)
     * @param onReceive callback function that gets called when server receives turn
     * @param onSend callback function that gets called when server sends decision
     * @return non-zero if server fails to start, 0 if server starts properly
     */
    fun startServer(port: Int,
                    onReceive: (turn: PlayerTurn) -> Unit,
                    onSend: (decision: PlayerDecision) -> Unit): Int {
        try {
            HttpServer.create(InetSocketAddress(port), 0).apply {
                createContext("/server") { exchange: HttpExchange ->
                    // read in input from server
                    // once the turn is parsed, use that turn to call a passed in function
                    val turn = PlayerTurn.parseFrom(exchange.requestBody)
                    logger.info("received playerTurn: " + turn.playerName)
                    onReceive(turn)

                    // calculate what to do with turn
                    val gameState = GameState(turn.gameState)
                    val decision: CharacterDecision = player.makeDecision(turn.playerName, gameState)
                    val proto: PlayerDecision = decision.buildProtoClassCharacterDecision()
                    val size: Long = proto.toByteArray().size.toLong()

                    // send back response
                    exchange.responseHeaders["Content-Type"] = "application/octet-stream"
                    exchange.sendResponseHeaders(200, size)
                    proto.writeTo(exchange.responseBody)
                    exchange.responseBody.flush()
                    exchange.responseBody.close()
                    onSend(proto)
                }
                start()
            }
            logger.info("Server started on port $port")
            return 0
        } catch (e: Exception) {
            logger.warning("Server failed to start on $port: ${e.message}")
            return 1
        }
    }
}
