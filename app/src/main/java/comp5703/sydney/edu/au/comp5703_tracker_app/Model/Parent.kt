package comp5703.sydney.edu.au.comp5703_tracker_app.Model


/*Parent has controller level 2*/
class Parent {
    var username: String = ""
    var controlLevel: String = ""
    var role: String = ""
    var creatorId: String = ""
    var phone: String = ""
    var dob: String = ""

    constructor() {
    }
    constructor(
        username: String,
        controlLevel: String,
        role: String,
        creatorId: String,
        phoneNum: String,
        dateOfBirth: String
    ) {
        this.username = username
        this.controlLevel = controlLevel
        this.role = role
        this.creatorId = creatorId
        this.phone = phoneNum
        this.dob = dateOfBirth
    }
    override fun toString(): String {
        return "Parent(username='$username', controlLevel='$controlLevel', " +
                "role='$role', creatorId='$creatorId', phoneNum='$phone', dateOfBirth='$dob')"
    }
}