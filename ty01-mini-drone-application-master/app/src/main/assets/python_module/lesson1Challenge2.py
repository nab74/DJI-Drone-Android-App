from djitellopy import Tello

def main():
    # connect to drone
    tello = Tello()
    tello.connect()

    # challenge 2: take off, land, pause, repeat
    tello.takeoff()
    tello.land()

    tello.takeoff()
    tello.land()

if __name__ == "__main__":
    main()
