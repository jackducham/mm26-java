package mech.mania.starter_pack.service_layer;

import mech.mania.engine.domain.game.GameState;
import mech.mania.engine.domain.game.characters.CharacterDecision;
import mech.mania.engine.domain.model.PlayerProtos;
import mech.mania.starter_pack.domain.PlayerStrategy;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.logging.Logger;

@SpringBootApplication
@RestController
public class EngineRESTHandler {

    private final Logger LOGGER = Logger.getLogger( getClass().getName() );
    private PlayerStrategy player = new PlayerStrategy();

    /**
     * Receiving PlayerTurn from engine
     * @param payload PlayerTurn protobuf
     * @return PlayerDecision protobuf from PlayerStrategy
     */
    @PostMapping("/server")
    public byte[] makeDecision(@RequestBody byte[] payload) {
        try {
            PlayerProtos.PlayerTurn turn = PlayerProtos.PlayerTurn.parseFrom(payload);
            GameState gameState = new GameState(turn.getGameState());
            String playerName = turn.getPlayerName();

            CharacterDecision decision = player.makeDecision(playerName, gameState);

            // Check for null decision
            if(decision == null) return null;
            return decision.buildProtoClassCharacterDecision().toByteArray();

        } catch(InvalidProtocolBufferException e){
            // Log error
            LOGGER.warning("InvalidProtocolBufferException on / request from Engine: " + e.getMessage());

            // Return no decision (do nothing)
            return null;
        }
    }
}
