package entidades;

public class Reservada implements Estado {
    @Override
    public void liberar(Mesa m) {
        m.setEstado(new Liberada());
    }

    @Override
    public void reservar(Mesa m) {
        throw new RuntimeException("La mesa ya esta reservada");
    }

    @Override
    public void ocupar(Mesa m) {
        m.setEstado(new Ocupada());
    }
    @Override
    public String nombreEstado() {
        return "reservada";
    }
}
