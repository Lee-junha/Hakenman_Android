package archiveasia.jp.co.hakenman.Activitiy

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import archiveasia.jp.co.hakenman.Model.Work
import archiveasia.jp.co.hakenman.R

const val INTENT_WORK_YEAR = "year"

class MonthWorkActivity : AppCompatActivity() {

    private lateinit var worksheetListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month_work)

        worksheetListView = findViewById(R.id.worksheet_listView)

        val item = intent.getParcelableExtra<Work>(INTENT_WORK_YEAR)
        println(item)
    }

    companion object {

        fun newIntent(context: Context, work: Work): Intent {
            val intent = Intent(context, MonthWorkActivity::class.java)
            intent.putExtra(INTENT_WORK_YEAR, work)
            return intent
        }
    }
}
