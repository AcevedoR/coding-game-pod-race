package acevedor.codinggame.podrace;

public class Checkpoint {
    int id;
    Point position;

    @Override
    public String toString() {
        return "Checkpoint{" +
                "  id=" + id +
                ", position=" + position +
                '}';
    }

    public Checkpoint(final int id, final Point position) {
        this.id = id;
        this.position = position;
    }
}
