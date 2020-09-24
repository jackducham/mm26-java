package mech.mania.engine.domain.model;

import mech.mania.starter_pack.domain.model.board.Board;
import mech.mania.starter_pack.domain.model.board.Tile;
import mech.mania.starter_pack.domain.model.characters.Character;
import mech.mania.starter_pack.domain.model.characters.Monster;
import mech.mania.starter_pack.domain.model.characters.Player;
import mech.mania.starter_pack.domain.model.characters.Position;
import mech.mania.starter_pack.domain.model.items.*;

import java.util.List;

public class ProtoFactory {
    public static CharacterProtos.Position Position(Position position) {
        CharacterProtos.Position.Builder positionBuilder = CharacterProtos.Position.newBuilder();
        positionBuilder.setX(position.getX());
        positionBuilder.setY(position.getY());
        positionBuilder.setBoardId(position.getBoardID());

        return positionBuilder.build();
    }

    public static BoardProtos.Tile Tile(Tile tile){
        Tile.TileType type = tile.getType();
        BoardProtos.Tile.Builder tileBuilder = BoardProtos.Tile.newBuilder();
        switch (type) {
            case VOID:
                tileBuilder.setTileType(BoardProtos.Tile.TileType.VOID);
                break;
            case BLANK:
                tileBuilder.setTileType(BoardProtos.Tile.TileType.BLANK);
                break;
            case IMPASSIBLE:
                tileBuilder.setTileType(BoardProtos.Tile.TileType.IMPASSIBLE);
                break;
            case PORTAL:
                tileBuilder.setTileType(BoardProtos.Tile.TileType.PORTAL);
                break;
        }

        List<Item> items = tile.getItems();
        for (int i = 0; i < items.size(); i++) {
            Item curItem = items.get(i);
            tileBuilder.setItems(i, Item(curItem));
        }

        return tileBuilder.build();
    }

    public static BoardProtos.Board Board(Board board){
        BoardProtos.Board.Builder boardBuilder = BoardProtos.Board.newBuilder();

        Tile[][] grid = board.getGrid();
        List<Position> portals = board.getPortals();
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                boardBuilder.addGrid(r * grid[c].length + c, Tile(grid[r][c]));
            }
        }

        for (int i = 0; i < portals.size(); i++) {
            boardBuilder.addPortals(i, Position(portals.get(i)));
        }

        boardBuilder.setRows(grid.length);
        boardBuilder.setColumns(grid[0].length);

        return boardBuilder.build();
    }

    private static CharacterProtos.Character Character(Character character) {
        CharacterProtos.Character.Builder characterBuilder = CharacterProtos.Character.newBuilder();

        characterBuilder.setName(character.getName());
        characterBuilder.setBaseSpeed(character.getBaseSpeed());
        characterBuilder.setBaseMaxHealth(character.getBaseMaxHealth());
        characterBuilder.setBaseAttack(character.getBaseAttack());
        characterBuilder.setBaseDefense(character.getBaseDefense());
        characterBuilder.setCurrentHealth(character.getCurrentHealth());
        characterBuilder.setExperience(character.getExperience());

        characterBuilder.setTicksSinceDeath(character.getTicksSinceDeath());
        characterBuilder.setIsDead(character.isDead());

        characterBuilder.setPosition(Position(character.getPosition()));
        characterBuilder.setSpawnPoint(Position(character.getSpawnPoint()));

        if(character.getWeapon() != null) {
            characterBuilder.setWeapon(Weapon(character.getWeapon()));
        }

        for (int i = 0; i < character.getActiveEffects().size(); i++) {
            characterBuilder.setActiveEffectsTempStatusModifier(i, TempStatusModifier(character.getActiveEffects().get(i).getFirst()));
            characterBuilder.setActiveEffectsSource(i, character.getActiveEffects().get(i).getSecond());
            characterBuilder.setActiveEffectsIsPlayer(i, character.getActiveEffects().get(i).getThird());
        }

        characterBuilder.putAllTaggedPlayersDamage(character.getTaggedPlayersDamage());

        return characterBuilder.build();
    }

    public static CharacterProtos.Monster Monster(Monster monster) {
        CharacterProtos.Character characterProtoClass = Character(monster);

        CharacterProtos.Monster.Builder monsterBuilder = CharacterProtos.Monster.newBuilder();
        monsterBuilder.mergeCharacter(characterProtoClass);
        monsterBuilder.setAggroRange(monster.getAggroRange());

        return monsterBuilder.build();
    }

    public static CharacterProtos.Player Player(Player player) {
        CharacterProtos.Character characterProtoClass = Character(player);
        CharacterProtos.Player.Builder playerBuilder = CharacterProtos.Player.newBuilder();

        playerBuilder.mergeCharacter(characterProtoClass);

        for (int i = 0; i < Player.INVENTORY_SIZE; i++) {
            Item curItem = player.getInventory()[i];
            playerBuilder.setInventory(i, Item(curItem));
        }

        if (player.getHat() != null) {
            playerBuilder.setHat(Hat(player.getHat()));
        }
        if (player.getClothes() != null) {
            playerBuilder.setClothes(Clothes(player.getClothes()));
        }
        if (player.getShoes() != null) {
            playerBuilder.setShoes(Shoes(player.getShoes()));
        }

        return playerBuilder.build();
    }

    public static ItemProtos.Item Item(Item item){
        ItemProtos.Item.Builder itemBuilder = ItemProtos.Item.newBuilder();

        if (item instanceof Clothes) {
            itemBuilder.setClothes(Clothes((Clothes)item));
        } else if (item instanceof Hat) {
            itemBuilder.setHat(Hat((Hat)item));
        } else if (item instanceof Shoes) {
            itemBuilder.setShoes(Shoes((Shoes)item));
        } else if (item instanceof Weapon) {
            itemBuilder.setWeapon(Weapon((Weapon)item));
        } else if (item instanceof Consumable) {
            itemBuilder.setConsumable(Consumable((Consumable)item));
        }

        return itemBuilder.build();
    }

    public static ItemProtos.Clothes Clothes(Clothes clothes) {
        ItemProtos.Clothes.Builder clothesBuilder = ItemProtos.Clothes.newBuilder();
        clothesBuilder.setStats(StatusModifier(clothes.getStats()));

        return clothesBuilder.build();
    }

    public static ItemProtos.Consumable Consumable(Consumable consumable) {
        ItemProtos.Consumable.Builder consumableBuilder = ItemProtos.Consumable.newBuilder();
        consumableBuilder.setStacks(consumable.getStacks());
        consumableBuilder.setEffect(TempStatusModifier(consumable.getEffect()));

        ItemProtos.Item.Builder itemBuilder = ItemProtos.Item.newBuilder();
        itemBuilder.setMaxStack(consumable.getMaxStack());
        itemBuilder.setConsumable(consumableBuilder.build());

        return consumableBuilder.build();
    }

    public static ItemProtos.Hat Hat(Hat hat) {
        ItemProtos.Hat.Builder hatBuilder = ItemProtos.Hat.newBuilder();

        hatBuilder.setStats(StatusModifier(hat.getStats()));
        // TODO: add HatEffect

        return hatBuilder.build();
    }

    public static ItemProtos.Shoes Shoes(Shoes shoes){
        ItemProtos.Shoes.Builder shoesBuilder = ItemProtos.Shoes.newBuilder();
        shoesBuilder.setStats(StatusModifier(shoes.getStats()));
        return shoesBuilder.build();
    }

    public static ItemProtos.StatusModifier StatusModifier(StatusModifier statusModifier) {
        ItemProtos.StatusModifier.Builder statusModifierBuilder = ItemProtos.StatusModifier.newBuilder();

        statusModifierBuilder.setFlatSpeedChange(statusModifier.getFlatSpeedChange());
        statusModifierBuilder.setPercentSpeedChange(statusModifier.getPercentSpeedChange());
        statusModifierBuilder.setFlatHealthChange(statusModifier.getFlatHealthChange());
        statusModifierBuilder.setPercentHealthChange(statusModifier.getPercentHealthChange());
        statusModifierBuilder.setFlatExperienceChange(statusModifier.getFlatExperienceChange());
        statusModifierBuilder.setPercentExperienceChange(statusModifier.getPercentExperienceChange());
        statusModifierBuilder.setFlatAttackChange(statusModifier.getFlatAttackChange());
        statusModifierBuilder.setPercentAttackChange(statusModifier.getPercentAttackChange());
        statusModifierBuilder.setFlatDefenseChange(statusModifier.getFlatDefenseChange());
        statusModifierBuilder.setPercentDefenseChange(statusModifier.getPercentDefenseChange());

        return statusModifierBuilder.build();
    }

    public static ItemProtos.TempStatusModifier TempStatusModifier(TempStatusModifier tempStatusModifier){
        ItemProtos.TempStatusModifier.Builder tempStatusModifierBuilder = ItemProtos.TempStatusModifier.newBuilder();

        tempStatusModifierBuilder.setStats(StatusModifier(tempStatusModifier));
        tempStatusModifierBuilder.setTurnsLeft(tempStatusModifier.getTurnsLeft());
        tempStatusModifierBuilder.setFlatDamagePerTurn(tempStatusModifier.getDamagePerTurn());

        return tempStatusModifierBuilder.build();
    }

    public static ItemProtos.Weapon Weapon(Weapon weapon) {
        ItemProtos.Weapon.Builder weaponBuilder = ItemProtos.Weapon.newBuilder();
        weaponBuilder.setStats(StatusModifier(weapon.getStats()));
        weaponBuilder.setRange(weapon.getRange());
        weaponBuilder.setSplashRadius(weapon.getSplashRadius());
        weaponBuilder.setOnHitEffect(TempStatusModifier(weapon.getOnHitEffect()));
        weaponBuilder.setAttack(weapon.getAttack());

        return weaponBuilder.build();
    }
}
