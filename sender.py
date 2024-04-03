import socket
import pyrealsense2 as rs
import numpy as np

def capture_image():
    # Configure depth and color streams
    pipeline = rs.pipeline()
    config = rs.config()
    config.enable_stream(rs.stream.depth, 640, 480, rs.format.z16, 30)
    config.enable_stream(rs.stream.color, 640, 480, rs.format.bgr8, 30)

    # Start streaming
    pipeline.start(config)

    try:
        # Wait for a coherent pair of frames: depth and color
        frames = pipeline.wait_for_frames()
        color_frame = frames.get_color_frame()

        if not color_frame:
            return None

        # Convert color image to numpy array
        color_image = np.asanyarray(color_frame.get_data())

        return color_image

    finally:
        pipeline.stop()

def send_image(image_data, host, port):
    # Create socket
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
    try:
        # Connect to server
        s.connect((host, port))
        print("Connected to server at {}:{}".format(host, port))

        # Convert image data to bytes
        image_bytes = image_data.tobytes()

        # Send image data size
        s.sendall(len(image_bytes).to_bytes(4, byteorder='big'))

        # Send image data
        s.sendall(image_bytes)

        print("Image sent to server")

    except socket.error as e:
        print("Error connecting to server:", e)

    finally:
        # Close socket
        s.close()

if __name__ == '__main__':
    # TCP/IP connection settings
    host = '192.168.158.54'  # Receiver's IP address
    port = 12345  # Example port number

    # Capture image
    image_data = capture_image()
    if image_data is not None:
        # Send image
        print("sending image")
        send_image(image_data, host, port)
        print("image sent")
