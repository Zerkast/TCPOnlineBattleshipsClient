package application;

public class Controller {
    private Model model;

    public Controller(Model model) {
        this.model = model;
    }

    public void placeBoat(int posX, int posY) {
        model.placeBoat(posX, posY);
    }
    public boolean shoot(int posX, int posY) {
        return model.shoot(posX, posY);
    }

    public void sendInvite(String username) {
        model.sendInvite(username);
    }

    public void sendInviteResponse(String username, Boolean response) {
        model.sendInviteResponse(username, response);
    }

    public void join(String username) {
        model.join(username);
    }

    public void quit() {
        model.quit();
    }
}
