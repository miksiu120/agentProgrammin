package pl.gda.pg.eti.kask.sa.migration;

import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import java.util.Scanner;

public class AddContainers {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Get the JADE runtime
            Runtime rt = Runtime.instance();

            // Define the main container's connection details
            String host = "localhost";
            int port = 11111;

            // Number of containers to add
            int numContainers = 3;

            // Add multiple containers
            for (int i = 1; i <= numContainers; i++) {
                System.out.print("Enter name for Container " + i + ": ");
                String containerName = scanner.nextLine();

                // Create a profile for each new container
                Profile profile = new ProfileImpl();
                profile.setParameter(Profile.MAIN_HOST, host);
                profile.setParameter(Profile.MAIN_PORT, String.valueOf(port));

                // Set the custom container name
                profile.setParameter(Profile.CONTAINER_NAME, containerName);

                // Create the container
                AgentContainer container = rt.createAgentContainer(profile);
                System.out.println("Container " + i + " added with name: " + containerName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}