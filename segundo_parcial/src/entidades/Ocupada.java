package entidades;

public class Ocupada implements Estado {
    @Override
    public void liberar(Mesa m) {
        m.setEstado(new Liberada());
    }

    @Override
    public void reservar(Mesa m) {
        // No se puede reservar una mesa que está ocupada
        throw new RuntimeException("La mesa ya esta ocupada");
    }

    @Override
    public void ocupar(Mesa m) {
        // No se puede ocupar una mesa que ya está ocupada
        throw new RuntimeException("La mesa ya esta ocupada");
    }
}
