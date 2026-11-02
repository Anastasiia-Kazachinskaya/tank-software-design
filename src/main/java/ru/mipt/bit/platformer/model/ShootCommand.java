package ru.mipt.bit.platformer.model;

/** Команда "выстрелить". */
public class ShootCommand implements Command {
    private final Shootable shooter;

    public ShootCommand(Shootable shooter) {
        this.shooter = shooter;
    }

    @Override
    public void execute() {
        shooter.shoot();
    }
}