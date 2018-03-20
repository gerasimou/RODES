package rodes.testing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class MainAnimationTimer extends Application {
    @Override
    public void start(Stage primaryStage) {

        final BlockingQueue<Integer> messageQueue = new ArrayBlockingQueue<>(1);

        TextArea console = new TextArea();

        Button startButton = new Button("Start");
        startButton.setOnAction(event -> {
//            MessageProducer producer = new MessageProducer(messageQueue);
        		TestRODES producer = new TestRODES(messageQueue);
//        		messageQueue = producer.textQueue;
            Thread t = new Thread(producer, "TestRODES");
            t.setDaemon(true);
            t.start();
        });

        final LongProperty lastUpdate = new SimpleLongProperty();

        final long minUpdateInterval = 0 ; // nanoseconds. Set to higher number to slow output.

        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (now - lastUpdate.get() > minUpdateInterval) {
//                    final Integer message = messageQueue.poll();
//                    if (message != null) {
//                        console.appendText("\n" + message);
//                    }
	    				List<Integer> list = new ArrayList<Integer>();
	    				messageQueue.drainTo(list);
	    				if (!list.isEmpty()) {
	                        console.appendText("\n" + list.toString());
	                      }
                    lastUpdate.set(now);
                }
            }

        };

        timer.start();

//        HBox controls = new HBox(5, startButton);
//        controls.setPadding(new Insets(10));
//        controls.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane(console, null, null, startButton, null);
        Scene scene = new Scene(root,600,400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static class MessageProducer implements Runnable {
        private final BlockingQueue<String> messageQueueI ;

        public MessageProducer(BlockingQueue<String> messageQueue) {
            this.messageQueueI = messageQueue ;
        }

        @Override
        public void run() {
            long messageCount = 0 ;
            try {
                while (true) {
                    final String message = "Message " + (++messageCount);
                    messageQueueI.put(message);
                }
            } catch (InterruptedException exc) {
                System.out.println("Message producer interrupted: exiting.");
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}