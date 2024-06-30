package parking

var parkingLot: MutableList<Car?> = ArrayList(0)

data class Car(val licensePlate: String, val color: String)
data class ParkingSpot(val parkingSpot: Int, val car: Car?)

fun main() {
    while (true) {
        val arguments = readln().split(" ")
        val command = arguments[0]
        if (command != "exit" && command != "create" && parkingLot.isEmpty()) {
            printParkingIsNotCreated()
            continue
        }
        when (command) {
            "exit" -> break
            "create" -> createParkingLot(arguments)
            "park" -> parkTheCar(arguments)
            "leave" -> leaveTheParking(arguments)
            "status" -> printStatusParking()
            "reg_by_color" -> printAllLicensePlatesByColor(arguments)
            "spot_by_color" -> printParkingSpacesByColor(arguments)
            "spot_by_reg" -> printParkingSpotByLicensePlate(arguments)
            else -> println("wrong command")
        }
    }
}

fun printParkingSpotByLicensePlate(arguments: List<String>) {
    val licensePlate = arguments[1]
    val parkingSpot = findParkingSpotByLicensePlate(licensePlate)
    println(parkingSpot ?: "No cars with registration number $licensePlate were found.")
}

fun findParkingSpotByLicensePlate(licensePlate: String): Int? {
    val occupiedParkingSpots = findOccupiedParkingSpots()
    return occupiedParkingSpots.find { it.car?.licensePlate == licensePlate }?.parkingSpot
}

fun printParkingSpacesByColor(arguments: List<String>) {
    val color = arguments[1].lowercase()
    val occupiedParkingSpots = findOccupiedParkingSpots()
    val parkingSpacesByColor =
        occupiedParkingSpots.filter { it.car?.color == color }.map { it.parkingSpot }.joinToString(", ")
    printFilterResultByColor(parkingSpacesByColor, color)

}

fun printAllLicensePlatesByColor(arguments: List<String>) {
    val color = arguments[1].lowercase()
    val licensePlatesByColor = parkingLot.filter { it?.color == color }.map { it?.licensePlate }.joinToString(", ")
    printFilterResultByColor(licensePlatesByColor, color)
}

private fun printFilterResultByColor(parkingSpacesByColor: String, color: String) {
    println(parkingSpacesByColor.ifEmpty { "No cars with color $color were found." })
}

fun printStatusParking() {
    val occupiedParkingSpots = findOccupiedParkingSpots()
    if (occupiedParkingSpots.isEmpty()) {
        println("Parking lot is empty.")
        return
    }
    occupiedParkingSpots.forEach { println("${it.parkingSpot} ${it.car?.licensePlate} ${it.car?.color}") }
}

private fun findOccupiedParkingSpots(): MutableList<ParkingSpot> {
    val occupiedParkingSpots = mutableListOf<ParkingSpot>()
    for (i in 0 until parkingLot.size) {
        if (parkingLot[i] != null) {
            val parkingSpot = ParkingSpot(i + 1, parkingLot[i])
            occupiedParkingSpots.add(parkingSpot)
        }
    }
    return occupiedParkingSpots
}

private fun printParkingIsNotCreated() {
    println("Sorry, a parking lot has not been created.")
}

fun createParkingLot(arguments: List<String>) {
    val amountOfSpots = arguments[1].toInt()
    if (amountOfSpots < 0) {
        println("creating a parking lot should have positive amount of parking spots.")
    }
    parkingLot = ArrayList(amountOfSpots)
    makeParkingSpots(amountOfSpots)
    println("Created a parking lot with $amountOfSpots spots.")
}

fun makeParkingSpots(amountOfSpots: Int) {
    repeat(amountOfSpots) {
        parkingLot.add(it, null)
    }
}

private fun leaveTheParking(arguments: List<String>) {
    val parkingSpot = arguments[1].toInt()
    val car = parkingLot.getOrNull(parkingSpot - 1)
    if (car != null) {
        parkingLot[parkingSpot - 1] = null
        println("Spot $parkingSpot is free.")
    } else println("There is no car in spot $parkingSpot.")
}

private fun parkTheCar(arguments: List<String>) {
    val car = Car(licensePlate = arguments[1], color = arguments[2].lowercase())
    val parkingSpot = park(car)
    if (parkingSpot != null) {
        println("${car.color.replaceFirstChar { it.uppercase() }} car parked in spot $parkingSpot.")
    } else {
        println("Sorry, the parking lot is full.")
    }
}

fun park(car: Car): Int? {
    for (i in parkingLot.indices) {
        if (parkingLot[i] == null) {
            parkingLot[i] = car
            return i + 1
        }
    }
    return null
}

