package comp5703.sydney.edu.au.comp5703_tracker_app.Model

/*child has controller level 1*/
class Child {

    var username: String = ""
    var controlLevel: String = ""
    var role: String = ""
    var creatorId: String = ""
    var phone: String = ""
    var dob: String = ""
    var score: Int = 0
    var completedTask: ArrayList<Task> ?= null
    var goal: ArrayList<Goal> ?= null


    constructor() {
    }

    constructor(
        username: String,
        controlLevel: String,
        role: String,
        creatorId: String,
        phoneNum: String,
        dateOfBirth: String,
        score: Int
    ) {
        this.username = username
        this.controlLevel = controlLevel
        this.role = role
        this.creatorId = creatorId
        this.phone = phoneNum
        this.dob = dateOfBirth
        this.score = score
    }

    //Use in Setting Main, creatorId is for child own id for temp store data
    constructor(username: String, id: String){
        this.username = username
        this.creatorId = id
    }

    override fun toString(): String {
        return "Child(username='$username', controlLevel='$controlLevel', role='$role', creatorId='$creatorId', phone='$phone', dob='$dob', score=$score, completedTask=$completedTask, goal=$goal)"
    }

    /*
    constructor(
        username: String,
        controlLevel: String,
        role: String,
        creatorId: String,
        phoneNum: String,
        dateOfBirth: String,
        score: Int,
        completedTask: ArrayList<Task>
    ) {
        this.username = username
        this.controlLevel = controlLevel
        this.role = role
        this.creatorId = creatorId
        this.phone = phoneNum
        this.dob = dateOfBirth
        this.score = score
        this.completedTask = completedTask
    }

     */


}