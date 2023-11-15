package entidades;

public class Mesa {
    private int id_mesa;
    private int nro_mesa;
    private int capacidad;
    private int consumo;
    private Estado estado;
    private Resto resto;  

    public Mesa(int id_mesa, int nro_mesa, int capacidad, int consumo, Estado estado, Resto resto) {
        this.id_mesa = id_mesa;
        this.nro_mesa = nro_mesa;
        this.capacidad = capacidad;
        this.consumo = consumo;
        this.estado = estado;
        this.resto = resto;
    }

    public Mesa() {
        super();
    }

    public int getId_mesa() {
        return id_mesa;
    }

    public void setId_mesa(int id_mesa) {
        this.id_mesa = id_mesa;
    }

    public int getNro_mesa() {
        return nro_mesa;
    }

    public void setNro_mesa(int nro_mesa) {
        this.nro_mesa = nro_mesa;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getConsumo() {
        return consumo;
    }

    public void setConsumo(int consumo) {
        this.consumo = consumo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Resto getResto() {
        return resto;
    }

    public void setResto(Resto resto) {
        this.resto = resto;
    }

    public void liberar() {
        estado.liberar(this);
    }

    public void reservar() {
        estado.reservar(this);
    }

    public void ocupar() {
        estado.ocupar(this);
    }
    
    public String toString() {
        return "Mesa [nro_mesa=" + nro_mesa + ", capacidad=" + capacidad + ", consumo=" + consumo + ", estado=" + estado.getClass().getSimpleName() + "]";
    }
}
