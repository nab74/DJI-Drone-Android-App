from djitellopy import Tello


def main():
    # connect to drone
    tello = Tello()
    tello.connect()

    # challenge 3: create your own combination
    tello.takeoff()

    tello.move_left(100)
    tello.rotate_counter_clockwise(90)
    tello.move_forward(100)

    tello.land()


if __name__ == "__main__":
    main()