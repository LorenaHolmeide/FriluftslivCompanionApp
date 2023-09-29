package no.hiof.friluftslivcompanionapp.models

class Bird(
    private var speciesName: String?,
    var speciesNameScientific: String,
    private var description: String?,
    var photoUrl: String?
) : Animal(
    speciesName,
    speciesNameScientific,
    description,
    photoUrl
) {

    override fun toString(): String {
        return StringBuilder().apply {
            append("\nname: $speciesName\n")
            append("scientific: $speciesNameScientific\n")
            append("photoUrl: $photoUrl\n")
            append("description: $description\n")
        }.toString()
    }
}