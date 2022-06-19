package comp5703.sydney.edu.au.comp5703_tracker_app.Model
/*Creator has controller level 3*/
class Creator {
    var username: String=""
    var email: String?=""
   // var password: String=""
    var phone:String=""
    var dob:String=""

    constructor(username: String,email:String,phone:String,dob:String) {
        this.username=username
        this.email=email
        this.phone=phone
        this.dob=dob
    }
    constructor() {
    }

    override fun toString(): String {
        return "Creator(Username='$username', Email=$email')"
    }

    //getter and setter is automatically generated inside this class because we are using var

}

