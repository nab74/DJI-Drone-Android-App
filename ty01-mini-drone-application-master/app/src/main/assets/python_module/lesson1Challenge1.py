from djitellopy import Tello

def main():
    # Connect to the drone
    tello = Tello()
    tello.connect()

    # Challenge 1: Adding a new block
    tello.takeoff()
    tello.land()


if __name__ == "__main__":
    main()
