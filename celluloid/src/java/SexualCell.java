class SexualCell extends Cell {
    private boolean isSeekingPartner = false;
    private final Cupid cupid;

    public SexualCell(FoodPool foodPool, Watcher watcher, Cupid cupid) {
        super(foodPool, watcher);
        this.cupid = cupid;
    }

    @Override
    public synchronized void reproduce() {
        if (!isSeekingPartner) {
            System.out.println(this.getName() + " is seeking a partner to reproduce.");
            isSeekingPartner = true;
            cupid.registerCell(this); // register with Cupid
        }
    }

    public synchronized void makeChild(SexualCell partner) {
        if (partner != null) {
            System.out.println(this.getName() + " found a partner: " + partner.getName());
            SexualCell child = new SexualCell(foodPool, watcher, cupid);
            child.start();
        }
    }
}
