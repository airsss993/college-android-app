package ru.dzhaparidze.collegeapp.features.schedule.utils

object GroupsCatalog {
    val allGroups: List<String> = listOf(
        // 2025 - 1 year
        "ИТ25-11", "ИТ25-12", "ИТ25-13", "ИТ25-14",
        "ИТ24-11", "ИТ24-12", "ИТ24-13", "ИТ24-14",
        "ИТ23-11", "ИТ23-12", "ИТ23-13",
        "ИТ22-11", "ИТ22-12"
    )

    fun courseYear(group: String): Int? {

    }
}

//static func courseYear(for group: String) -> Int? {
//    guard let year = GroupSubgroupCompatibility.getYear(from: group),
//    let yearInt = Int(year) else {
//        return nil
//    }
//
//    return 26 - yearInt
//}