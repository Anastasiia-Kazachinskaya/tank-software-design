package ru.mipt.bit.platformer.model;

public class ToggleHealthBarsCommand implements Command {
    private final HealthBarsToggle toggle;

    public ToggleHealthBarsCommand(HealthBarsToggle toggle) {
        this.toggle = toggle;
    }

    @Override
    public void execute() {
        toggle.toggleHealthBars();
    }
}