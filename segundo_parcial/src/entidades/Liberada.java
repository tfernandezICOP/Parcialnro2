package entidades;

public class Liberada implements Estado {
    @Override
    public void liberar(Mesa m) {
        throw new RuntimeException("La mesa ya esta liberada");
    }

    @Override
    public void reservar(Mesa m) {
        m.setEstado(new Reservada());
    }

    @Override
    public void ocupar(Mesa m) {
        
        throw new RuntimeException("La mesa no puede ser ocupada si esta liberada");
    }
    @Override
    public String nombreEstado() {
        return "liberada";
    }
}
