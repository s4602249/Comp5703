package comp5703.sydney.edu.au.comp5703_tracker_app.Model

class Record {
    var record_from:String = ""

    var record_type:String =""
    var record_name:String =""
    constructor(record_type:String,record_name:String,record_from:String)
    {

        this.record_type=record_type
        this.record_name=record_name
        this.record_from=record_from

    }

    override fun toString(): String {
        return "Record(record_type:'$record_type',record_name:'$record_name'record_from:'$record_from')"
    }

}