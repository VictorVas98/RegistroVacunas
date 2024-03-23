import java.io.*;
import java.util.*;

class Vacuna {
    String nombre;
    String fecha;

    public Vacuna(String nombre, String fecha) {
        this.nombre = nombre;
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "{\"vacuna\":\"" + nombre + "\",\"fecha\":\"" + fecha + "\"}";
    }
}

public class Main{
    private static final String ARCHIVO_VACUNAS = "C:\\Users\\Victo\\IdeaProjects\\RegistroVacunasEstructuraHash\\src\\vacunas.txt";
    private static HashMap<String, ArrayList<Vacuna>> registro = new HashMap<>();

    public static void main(String[] args) {
        cargarDatos();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Buscar persona por CUI");
            System.out.println("2. Mostrar todos los registros de vacunas");
            System.out.println("3. Salir");
            System.out.print("Ingrese su opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    buscarPersona(scanner);
                    break;
                case 2:
                    mostrarRegistros();
                    break;
                case 3:
                    guardarDatos();
                    System.out.println("¡Hasta luego!");
                    return;
                default:
                    System.out.println("Opción inválida. Por favor, ingrese una opción válida.");
            }
        }
    }

    private static void buscarPersona(Scanner scanner) {
        System.out.print("Ingrese el CUI de la persona a buscar: ");
        String cui = scanner.nextLine();
        if (registro.containsKey(cui)) {
            System.out.println("Registro de vacunas para el CUI " + cui + ":");
            ArrayList<Vacuna> vacunas = registro.get(cui);
            for (Vacuna vacuna : vacunas) {
                System.out.println(vacuna);
            }
        } else {
            System.out.println("No existe ninguna persona registrada con el CUI " + cui);
        }
    }

    private static void mostrarRegistros() {
        System.out.println("Registros de vacunas:");
        for (Map.Entry<String, ArrayList<Vacuna>> entry : registro.entrySet()) {
            System.out.println("\"CUI\":\"" + entry.getKey() + "\":" + entry.getValue());
        }
    }

    private static void cargarDatos() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_VACUNAS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(":\\[\\{");
                String cui = partes[0].split("\"")[1];
                String[] vacunas = partes[1].split("\\},\\{");
                ArrayList<Vacuna> listaVacunas = new ArrayList<>();
                for (String vacuna : vacunas) {
                    String[] datosVacuna = vacuna.split(",\"");
                    String nombre = datosVacuna[0].split("\":\"")[1];
                    String fecha = datosVacuna[1].split("\":\"")[1].replace("\"}", "");
                    listaVacunas.add(new Vacuna(nombre, fecha));
                }
                registro.put(cui, listaVacunas);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void guardarDatos() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_VACUNAS))) {
            for (Map.Entry<String, ArrayList<Vacuna>> entry : registro.entrySet()) {
                bw.write("\"CUI\":\"" + entry.getKey() + "\":" + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}