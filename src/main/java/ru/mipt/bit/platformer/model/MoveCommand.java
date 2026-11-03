package ru.mipt.bit.platformer.model;

/** Команда "поехать в направлении". */
public class MoveCommand implements Command {
    private final Movable movable;
    private final Direction direction;

    public MoveCommand(Movable movable, Direction direction) {
        this.movable = movable;
        this.direction = direction;
    }

    @Override
    public void execute() {
        movable.move(direction);
    }
}