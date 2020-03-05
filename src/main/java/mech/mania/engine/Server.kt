package mech.mania.engine

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import mech.mania.engine.server.communication.player.model.PlayerProtos.PlayerTurn
import mech.mania.engine.server.communication.player.model.PlayerProtos.PlayerDecision
import mech.mania.PlayerStrategyInterface
import mech.mania.PlayerStrategy
import java.io.IOException
import java.net.InetSocketAddress
import java.util.logging.Logger


class Server {

    private val logger = Logger.getLogger(Server::class.toString())
    private val player: PlayerStrategyInterface = PlayerStrategy()

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
                    exchange.responseHeaders["Content-Type"] = "application/octet-stream"

                    // read in input from server
                    // once the turn is parsed, use that turn to call a passed in function
                    val turn = PlayerTurn.parseFrom(exchange.requestBody)
                    onReceive(turn)

                    // calculate what to do with turn
                    val decision: PlayerDecision = player.makeDecision(turn)
                    val size: Long = decision.toByteArray().size.toLong()

                    // send back response
                    exchange.sendResponseHeaders(200, size)
                    decision.writeTo(exchange.responseBody)
                    onSend(decision)
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