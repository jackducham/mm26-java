package mech.mania.starter_pack

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mech.mania.starter_pack.entrypoints.Server
import mech.mania.engine.domain.model.InfraProtos.InfraPlayer
import mech.mania.engine.domain.model.InfraProtos.InfraStatus
import mech.mania.engine.domain.model.PlayerProtos.PlayerDecision
import mech.mania.engine.domain.model.PlayerProtos.PlayerTurn
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.net.HttpURLConnection
import java.net.ServerSocket
import java.net.URISyntaxException
import java.net.URL
import java.util.*
import java.util.concurrent.*
import java.util.logging.Logger
import kotlin.collections.ArrayList


/*
 * Follow this blog post:
 * https://medium.com/@MelvinBlokhuijzen/spring-websocket-endpoints-integration-testing-180357b4f24c
 */

@RunWith(SpringJUnit4ClassRunner::class)
class GameEngineCommunicationTests {

    /** Ports to launch the Game server on */
    private val infraPort = 8080
    private val visPort = 8081

    /** URL that visualizer will connect to */
    private var VISUALIZER_URL: String = "ws://localhost:$visPort/visualizer"

    /** URL that infra will send new/reconnect player messages to */
    private var INFRA_NEW_URL: String = "http://localhost:$infraPort/infra/player/new"
    private var INFRA_RECONNECT_URL: String = "http://localhost:$infraPort/infra/player/reconnect"

    private var LOGGER = Logger.getLogger(GameEngineCommunicationTests::class.toString())

    /** jar file to run to start engine */
    private var jarfile = "engine.jar"

    private lateinit var process: Process

    /**
     * Set up the testing environment by initializing variables and starting the game server.
     */
    @Before
    fun setup() {
        // start game server
        val pb = ProcessBuilder()
        pb.command("java", "-jar", jarfile, "$infraPort", "$visPort")
        process = pb.start()

         GlobalScope.launch {
             val reader = process.inputStream.reader()
             while (process.isAlive) {
                 LOGGER.info(reader.readText())
             }
         }

        LOGGER.info("Starting game engine...")
        awaitEngineStart()
        LOGGER.info("Game engine started")
    }

    /**
     * Clean up afterwards by sending a HTTP GET request to /infra/endgame.
     */
    @After
    fun cleanup() {
        // end game server - send HTTP request to server
        // https://stackoverflow.com/questions/46177133/http-request-in-kotlin
        val url = URL("http://localhost:$infraPort/infra/endgame")
        try {
            val bytes = url.readBytes()
            val statusObj = InfraStatus.parseFrom(bytes)
            LOGGER.info("Response upon sending endgame signal: ${statusObj.message}")
            Thread.sleep(1000)
        } catch (e: Exception) {
            // if the server has already closed, then ignore
            LOGGER.warning("Error closing server: ${e.message}")
        }
        process.destroy()
    }

    /**
     * Helper function that waits until engine /health endpoint responds
     */
    private fun awaitEngineStart(){
        while(true){
            // Connect to engine health endpoint
            val url = URL("http://localhost:$infraPort/infra/health")
            try {
                val bytes = url.readBytes()
                val statusObj = InfraStatus.parseFrom(bytes)
                // LOGGER.info("Waiting for engine to start. Health endpoint returned: " + statusObj.message)
                if(statusObj.status == 200){
                    Thread.sleep(8000) // Wait for server to fully boot (if some tests fail, try upping this time)
                    return
                }
            } catch (e: Exception) {
                // LOGGER.info("Waiting for engine to start. Exception received: " + e.message)
                Thread.sleep(1000)
                continue; // Try again
            }
        }
    }

    /**
     * Helper function that creates player servers with random names + ip addresses, sends POST
     * requests to the game to add those players to the game.
     */
    private fun connectNPlayers(n: Int,
                                onReceive: (turn: PlayerTurn) -> Unit,
                                onSend: (decision: PlayerDecision) -> Unit) {
        val playerNames: ArrayList<String> = ArrayList()
        val playerAddrs: ArrayList<String> = ArrayList()

        for (i in 0 until n) {
            // find a free port
            var socket: ServerSocket?
            try {
                socket = ServerSocket(0)
                socket.close()
            } catch (e: Exception) {
                LOGGER.warning("No more free ports found: " + e.message)
                return
            }
            val randomPort: Int = socket.localPort

            // start the server
            val server = Server()
            server.startServer(randomPort, onReceive, onSend)

            // gather information about the player
            val playerName = UUID.randomUUID().toString()
            val playerAddr = "localhost:$randomPort/server"
            LOGGER.info("Creating player \"$playerName\" with IP address $playerAddr")

            playerNames.add(playerName)
            playerAddrs.add(playerAddr)
        }

        for (i in 0 until n) {
            with (URL(INFRA_NEW_URL).openConnection() as HttpURLConnection) {
                requestMethod = "POST"
                doOutput = true
                setRequestProperty("Content-Type", "application/octet-stream")

                InfraPlayer.newBuilder()
                        .setPlayerIp(playerAddrs[i])
                        .setPlayerName(playerNames[i])
                        .build()
                        .writeTo(outputStream)
                connect()

                outputStream.flush()
                outputStream.close()

                val status: InfraStatus = InfraStatus.parseFrom(inputStream.readBytes())
                LOGGER.info("Response upon sending new player: " + status.message)

                inputStream.close()
                disconnect()
            }
        }
    }

    /**
     * Test to see if the endpoint works and can be connected to via websocket
     */
    @Test
    @Throws(URISyntaxException::class, InterruptedException::class, ExecutionException::class, TimeoutException::class)
    fun canReceivePlayerTurn() {
        // wait for an actual object to end the test
        val latch = CountDownLatch(2)

        connectNPlayers(1, {
            // if server receives turn successfully
            latch.countDown()
        }, {
            // if server sends turn successfully
            latch.countDown()
        })
        assert(latch.await(5, TimeUnit.SECONDS))
    }

    /**
     * Test to see if the endpoint works and can be connected to via websocket over multiple turns
     */
    @Test
    @Throws(URISyntaxException::class, InterruptedException::class, ExecutionException::class, TimeoutException::class)
    fun canReceivePlayerTurnMultipleTimes() {
        // wait for an actual object to end the test
        val latch = CountDownLatch(10) // tests for 5 turns

        connectNPlayers(1, {
            // if server receives turn successfully
            latch.countDown()
        }, {
            // if server sends turn successfully
            latch.countDown()
        })
        assert(latch.await(25, TimeUnit.SECONDS))
    }

    /**
     * Test to see if the endpoint works and can be connected to via websocket
     */
    @Test
    @Throws(URISyntaxException::class, InterruptedException::class, ExecutionException::class, TimeoutException::class)
    fun canReceiveMultiplePlayerTurns() {
        // wait for an actual object to end the test
        val latch = CountDownLatch(5)

        connectNPlayers(5, {
            // if server receives turn successfully
            latch.countDown()
        }, {
            // if server sends turn successfully
            latch.countDown()
        })

        assert(latch.await(25, TimeUnit.SECONDS))
    }
}