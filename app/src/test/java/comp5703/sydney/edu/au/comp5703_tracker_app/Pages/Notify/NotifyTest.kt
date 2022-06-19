package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify

import android.app.Activity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import org.junit.Assert.*
import org.junit.Test

class NotifyTest {




    var itemlist1 = ArrayList<String>( )

    var itemlist2 = ArrayList<String>()


    var itemlist3 = ArrayList<String>( )

    var itemlist4 = ArrayList<String>()

    var itemlist5 = ArrayList<String>( )

    var itemlist6 = ArrayList<String>()

    var item1 = "a"

    var item2 = "d"

    var item3 = "g"

    @Test
    fun test_user_identifier() {

         itemlist1.add("a" )
itemlist1.add("b"  )
        itemlist1.add("c" )
        itemlist2.add("d"  )
        itemlist2.add("e"  )
        itemlist2.add("f"  )

        itemlist3.add("d" )
        itemlist3.add("e"  )
        itemlist3.add("f" )
        itemlist4.add("g"  )
        itemlist4.add("h"  )
        itemlist4.add("i"  )

        itemlist5.add("g" )
        itemlist5.add("h"  )
        itemlist5.add("i" )
        itemlist6.add("j"  )
        itemlist6.add("k"  )
        itemlist6.add("l"  )

        var itemlista = ArrayList<String>()










        itemlista.add("f")
        itemlista.add("e")
        itemlista.add("d")
        itemlista.add("c")
        itemlista.add("b")








        var itemlistb = ArrayList<String> ()





        itemlistb.add("i")
        itemlistb.add("h")
        itemlistb.add("g")
        itemlistb.add("f")
        itemlistb.add("e")








        var itemlistc = ArrayList<String>()










        itemlistc.add("l")
        itemlistc.add("k")
        itemlistc.add("j")
        itemlistc.add("i")
        itemlistc.add("h")

        assertEquals( itemlista, RecordAdapter(Activity(), R.layout.notification_list_item , ArrayList()).itemlistchange( itemlist1 , itemlist2  , item1 )
            )
        assertEquals( itemlistb , RecordAdapter(Activity(), R.layout.notification_list_item , ArrayList()).itemlistchange( itemlist3 , itemlist4  , item2 )
        )
        assertEquals(  itemlistc  ,  RecordAdapter(Activity(), R.layout.notification_list_item , ArrayList()).itemlistchange( itemlist5 , itemlist6  , item3 )
        )



    }
}