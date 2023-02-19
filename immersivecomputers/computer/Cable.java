package immersivecomputers.computer;

public class Cable {
    private Cable previous, next;

    public Cable getNext() {
        return next;
    }

    public Cable getPrevious() {
        return previous;
    }

    public void setPrevious(Cable previous) {
        this.previous = previous;
    }

    public void setNext(Cable next) {
        this.next = next;
    }

    public void breakCable() {
        this.next.breakCable();
    }
}
