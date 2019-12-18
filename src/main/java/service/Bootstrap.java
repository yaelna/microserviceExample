package service;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import service.api.UpperCaseService;
import service.impl.UpperCaseImpl;
import service.kafka.KafkaProcessor;
import service.rest.RestService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bootstrap {
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws Exception {
        UpperCaseService service = new UpperCaseImpl();
        Server jettyServer = initRestServer(service);
        Runnable kafkaProcessor = new KafkaProcessor(service);

        try {
            executorService.submit(kafkaProcessor);
            jettyServer.start();
            jettyServer.join();
        } finally {
            executorService.shutdownNow();
            jettyServer.destroy();
        }
    }

    private static Server initRestServer(UpperCaseService service) {
        Server jettyServer = new Server(8080);

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");

        ResourceConfig resourceConfig = new ResourceConfig().register(new RestService(service));
        handler.addServlet(new ServletHolder(new ServletContainer(resourceConfig)), "/*");

        jettyServer.setHandler(handler);

        return jettyServer;
    }
}
