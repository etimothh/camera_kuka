import java.io.*;
import java.net.*;

public class ImageReceiver {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int triggerPort = 12345;
        int imagePort = 54321;

        try {
            // Connect to the image sender
            System.out.println("Trying to connect to image sender at " + host + ":" + imagePort);
            Socket imageSocket = new Socket(host, imagePort);
            System.out.println("Connected to image sender at " + host + ":" + imagePort);

            // Receive image data
            System.out.println("Receiving image data...");
            int dataSize;
            byte[] dataBuffer = new byte[1024];
            InputStream inputStream = imageSocket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            // Read the size of the data
            dataSize = dataInputStream.readInt();
            System.out.println("Received data size: " + dataSize);

            // Read the data itself
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (dataSize > 0) {
                int bytesRead = inputStream.read(dataBuffer, 0, Math.min(dataSize, dataBuffer.length));
                byteArrayOutputStream.write(dataBuffer, 0, bytesRead);
                dataSize -= bytesRead;
            }

            // Save the image data to a file
            String filePath = "received_image.jpg";
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            byteArrayOutputStream.writeTo(fileOutputStream);
            fileOutputStream.close();
            System.out.println("Image data saved to " + filePath);

            // Close image socket
            imageSocket.close();
            System.out.println("Image connection closed");

            // Trigger image capture
            System.out.println("Trying to trigger image capture at " + host + ":" + triggerPort);
            Socket triggerSocket = new Socket(host, triggerPort);
            System.out.println("Connected to trigger server at " + host + ":" + triggerPort);

            // Send trigger command
            PrintWriter out = new PrintWriter(triggerSocket.getOutputStream(), true);
            System.out.println("Sending trigger command...");
            out.println("trigger");

            // Close the trigger connection
            triggerSocket.close();
            System.out.println("Trigger connection closed");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
