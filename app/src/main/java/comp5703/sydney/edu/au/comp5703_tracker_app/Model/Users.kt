package comp5703.sydney.edu.au.comp5703_tracker_app.Model

class Users(val username: String, val controlLevel: String, val role: String, val creatorId: String) {
    constructor():this("","","","")
}

class creatoruser(val username: String,val email:String,val password:String,val phone:String,val dob:String){
    constructor():this("","","","","")

}

class Parents(val username: String,val creatorId: String,val phone:String,val dob:String){
    constructor():this("","","","")

}
