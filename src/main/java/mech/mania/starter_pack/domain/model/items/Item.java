package mech.mania.starter_pack.domain.model.items;

public abstract class Item {
    protected int maxStack;
    protected String name;
    protected int turnsToDeletion;

    public Item(int maxStack) {
        this.maxStack = maxStack;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public int getTurnsToDeletion() {
        return turnsToDeletion;
    }
}
