package cinema


fun main() {

    println("Enter the number of rows:")
    val rows = readLine()!!.toInt()
    println("Enter the number of seats in each row:")
    val seats = readLine()!!.toInt()
    val cinemaMap = Array(rows) { IntArray(seats) }
    var ocuppiedSeatsMap = mutableMapOf<Int, HashSet<Int>>()

    do {
        println("1. Show the seats")
        println("2. Buy a ticket")
        println("3. Statistics")
        println("0. Exit")

        ocuppiedSeatsMap = when (readln().toInt()) {
            1 -> showSeats(cinemaMap, ocuppiedSeatsMap)
            2 -> buyTicket(cinemaMap, ocuppiedSeatsMap)
            3 -> statistics(cinemaMap, ocuppiedSeatsMap)
            0 -> break
            else -> {
                println("Wrong input!")
                ocuppiedSeatsMap
            }
        }
    } while (true)
}

fun statistics(
    cinemaMap: Array<IntArray>, ocuppiedSeatsMap: MutableMap<Int, HashSet<Int>>
): MutableMap<Int, HashSet<Int>> {

    //The number of purchased tickets
    val totalNumberTicketsSold = ocuppiedSeatsMap.map { it.value.size }.sum()
    println("Number of purchased tickets: $totalNumberTicketsSold")

    //The number of purchased tickets represented as a percentage.
    val numberOfSeats = cinemaMap.size * cinemaMap[0].size
    val percentageOfTicketsSold = calculateNumberTicketsSoldPercentage(totalNumberTicketsSold, numberOfSeats)
    val formatPercentage = "%.2f".format(percentageOfTicketsSold)
    println("Percentage: $formatPercentage%")

    //Current income
    val total = calculateTotalSumSoldTicket(ocuppiedSeatsMap, cinemaMap)
    println("Current income: $$total")

    //The total income that shows how much money the theatre will get if all the tickets are sold
    val totalIncome = calculateTotalIncomeForCinema(cinemaMap)
    println("Total income: $$totalIncome")

    return ocuppiedSeatsMap
}

private fun calculateTotalSumSoldTicket(
    soldSeatMap: MutableMap<Int, HashSet<Int>>, cinemaMap: Array<IntArray>
): Int {
    val totalAmountTicketsSold = mutableListOf<Int>()
    soldSeatMap.forEach {
        for (i in 0 until it.value.size) {
            val amount = calculateTicketPrice(cinemaMap, it.key)
            totalAmountTicketsSold.add(amount)
        }
    }
    return totalAmountTicketsSold.sum()
}

fun calculateTotalIncomeForCinema(cinemaMap: Array<IntArray>): Int {
    val totalAmountTicketsSold = mutableListOf<Int>()
    for (i in cinemaMap.indices) {
        for (j in 0 until cinemaMap[i].size) {
            val result = calculateTicketPrice(cinemaMap, i + 1)
            totalAmountTicketsSold.add(result)
        }
    }
    return totalAmountTicketsSold.sum()
}

fun calculateNumberTicketsSoldPercentage(totalNumberTicketsSold: Int, numberOfSeats: Int): Double {
    return (totalNumberTicketsSold.toDouble() / numberOfSeats.toDouble()) * 100
}

fun buyTicket(cinemaMap: Array<IntArray>, seatsMap: MutableMap<Int, HashSet<Int>>): MutableMap<Int, HashSet<Int>> {
    var isSeatFreeLoop = true
    do {
        println("Enter a row number:")
        val chosenRow = readLine()!!.toInt()
        println("Enter a seat number in that row:")
        val chosenSeat = readLine()!!.toInt()

        val isValid = validadeSeat(cinemaMap, chosenRow, chosenSeat)

        if (isValid) {
            try {
                val isSeatFree = verifyIfSeatIsFree(seatsMap, chosenRow, chosenSeat)
                if (!isSeatFree) {
                    println("That ticket has already been purchased!")
                    isSeatFreeLoop = false
                } else {
                    isSeatFreeLoop = true
                    println("Ticket price: $${calculateTicketPrice(cinemaMap, chosenRow)}")
                    seatsMap[chosenRow] = seatsMap.getOrDefault(chosenRow, HashSet()).apply { add(chosenSeat) }
                }
                showSeats(cinemaMap, seatsMap)
            } catch (e: Exception) {
                println("Invalid input")
            }
        } else {
            println("Wrong input!")
        }
    } while (!isSeatFreeLoop)
    return seatsMap.toMutableMap()
}

fun verifyIfSeatIsFree(seatsMap: MutableMap<Int, java.util.HashSet<Int>>, chosenRow: Int, chosenSeat: Int): Boolean {
    if (seatsMap.contains(chosenRow)) {
        if (seatsMap[chosenRow]!!.contains(chosenSeat)) {
            return false
        }
    } else if (seatsMap.containsKey(chosenRow)) {
        return false
    }
    return true
}

fun validadeSeat(cinemaMap: Array<IntArray>, chosenRow: Int, chosenSeat: Int): Boolean {
    if (chosenRow < 0 || chosenRow > cinemaMap.size) {
        return false
    }
    if (chosenSeat < 0 || chosenSeat > cinemaMap[chosenRow - 1].size) {
        return false
    }
    return true
}


fun showSeats(cinemaMap: Array<IntArray>, seatsMap: MutableMap<Int, HashSet<Int>>): MutableMap<Int, HashSet<Int>> {
    println("Cinema:")
    printSeatIndex(cinemaMap)
    for (i in cinemaMap.indices) {
        print("${i + 1} ")
        for (j in cinemaMap[i].indices) {
            if (seatsMap.containsKey(i + 1) && seatsMap[i + 1] != null && seatsMap[i + 1]?.contains(j + 1) == true) {
                print("B ")
            } else {
                print("S ")
            }
        }
        println()
    }
    return seatsMap
}

fun calculateTicketPrice(cinemaMap: Array<IntArray>, row: Int): Int {
    val numberOfSeats = cinemaMap.size * cinemaMap[0].size
    return if (numberOfSeats <= 60) {
        10
    } else {
        val firstHalf = cinemaMap.size / 2
        val secondHalf = cinemaMap.size - firstHalf
        if (row >= secondHalf) {
            8
        } else {
            10
        }
    }
}

fun calculateIncome(rows: Int, seats: Int): String {
    val income: Int
    val numberOfSeats = rows * seats
    if (numberOfSeats <= 60) {
        return (numberOfSeats * 10).toString()
    } else {
        val firstHalf = rows / 2
        val secondHalf = rows - firstHalf
        val firstHalfSeats = firstHalf * seats
        val secondHalfSeats = secondHalf * seats
        val firstHalfIncome = firstHalfSeats * 10
        val secondHalfIncome = secondHalfSeats * 8
        income = firstHalfIncome + secondHalfIncome
    }

    return income.toString()
}

private fun printSeatIndex(cinemaMap: Array<IntArray>) {
    for (i in cinemaMap[0].indices) {
        if (i == 0) {
            print("  ")
        } else {
            print("$i ")
        }
    }
    println("${cinemaMap[0].size}")
}
