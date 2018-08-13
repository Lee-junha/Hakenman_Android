package archiveasia.jp.co.hakenman.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import archiveasia.jp.co.hakenman.R
import archiveasia.jp.co.hakenman.Adapter.WorkAdapter
import archiveasia.jp.co.hakenman.Manager.WorksheetManager
import archiveasia.jp.co.hakenman.Model.Worksheet
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val testJson = """
            [
            {
            "workDate": "2018/05/24 19:31:02",
            "workTimeSum": 150.25,
            "workDaySum": 20,
            "detailWorkList": [
                {
                "workYear": 2018,
                "workMonth": 08,
                "workDay": 25,
                "workWeek": "月",
                "workFlag": true,
                "beginTime": "2018/05/24 09:00:02",
                "endTime": "2018/05/24 18:31:02",
                "breakTime": 1,
                "note": "note1"
                },
                {
                "workYear": 2018,
                "workMonth": 08,
                "workDay": 25,
                "workWeek": "月",
                "workFlag": true,
                "beginTime": "2018/05/24 09:00:02",
                "endTime": "2018/05/24 18:31:02",
                "breakTime": 1,
                "note": "note1"
                },
                {
                "workYear": 2018,
                "workMonth": 08,
                "workDay": 25,
                "workWeek": "月",
                "workFlag": true,
                "beginTime": "2018/05/24 09:00:02",
                "endTime": "2018/05/24 18:31:02",
                "breakTime": 1,
                "note": "note1"
                }
            ]
            },
            {
            "workDate": "2018/05/24 19:31:02",
            "workTimeSum": 150.25,
            "workDaySum": 20,
            "detailWorkList": [
                {
                "workYear": 2018,
                "workMonth": 08,
                "workDay": 25,
                "workWeek": "月",
                "workFlag": true,
                "beginTime": "2018/05/24 09:00:02",
                "endTime": "2018/05/24 18:31:02",
                "breakTime": 1,
                "note": "note1"
                },
                {
                "workYear": 2018,
                "workMonth": 08,
                "workDay": 25,
                "workWeek": "月",
                "workFlag": true,
                "beginTime": "2018/05/24 09:00:02",
                "endTime": "2018/05/24 18:31:02",
                "breakTime": 1,
                "note": "note1"
                },
                {
                "workYear": 2018,
                "workMonth": 08,
                "workDay": 25,
                "workWeek": "月",
                "workFlag": true,
                "beginTime": "2018/05/24 09:00:02",
                "endTime": "2018/05/24 18:31:02",
                "breakTime": 1,
                "note": "note1"
                }
            ]
            }
            ]
            """

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adaptListView()

        // FloatingActionButton リスナー設定
        fab.setOnClickListener { view ->
            showCreateWorksheetDialog()
        }
    }

    private fun showAlertDialog(completion: () -> Unit) {
        val alertDialog = AlertDialog.Builder(this)
        with (alertDialog) {
            setTitle("すでに存在している勤務表です。\n上書きしますか？")

            setPositiveButton("確認") {
                dialog, whichButton ->
                completion
            }

            setNegativeButton("キャンセル") {
                dialog, whichButton ->
                dialog.dismiss()
            }
        }
        val dialog = alertDialog.create()
        dialog.show()
    }

    private fun showCreateWorksheetDialog() {
        val alertDialog = AlertDialog.Builder(this)
        var editTextAge: EditText? = null

        with (alertDialog) {
            setTitle("勤務表生成")

            editTextAge = EditText(context)
            editTextAge!!.hint="201808(yyyyDD)"
            editTextAge!!.inputType = InputType.TYPE_CLASS_NUMBER

            setPositiveButton("確認") {
                dialog, whichButton ->
                val editTextValue = editTextAge!!.text
                // TODO: Valiateする他の方法考えてみる
                if (editTextValue.isNullOrBlank()) {
                    Toast.makeText(this@MainActivity, "空欄なく入力してください。", Toast.LENGTH_SHORT).show()
                    showCreateWorksheetDialog()
                } else if (editTextValue.trim().length != 6) {
                    Toast.makeText(this@MainActivity, "正しい値を入力してください。", Toast.LENGTH_SHORT).show()
                    showCreateWorksheetDialog()
                } else {
                    var yearMonth = editTextAge!!.text.toString()
                    var worksheet = WorksheetManager.createWorksheet(yearMonth)

                    if (WorksheetManager.isAlreadyExistWorksheet(yearMonth)) {
                        showAlertDialog {
                            addNewWorksheet(worksheet)
                        }
                    } else {
                        addNewWorksheet(worksheet)
                    }

                    dialog.dismiss()
                }


            }

            setNegativeButton("キャンセル") {
                dialog, whichButton ->
                dialog.dismiss()
            }
        }

        val dialog = alertDialog.create()
        dialog.setView(editTextAge)
        dialog.show()
    }

    private fun addNewWorksheet(worksheet: Worksheet) {
        // 존재하지 않으면 제이슨 파일에 추가
        WorksheetManager.addWorksheetToJsonFile(worksheet)
        WorksheetManager.loadLocalWorksheet()
        val worksheetList = WorksheetManager.getWorksheetList()
        adaptListView()
        // 다시 리스트뷰 리로드
        work_listView.invalidateViews()
    }

    private fun adaptListView() {
        WorksheetManager.loadLocalWorksheet()

        var worksheetList = WorksheetManager.getWorksheetList()

        if (worksheetList.isNotEmpty()) {
            val adapter = WorkAdapter(this, worksheetList)

            work_listView.adapter = adapter
            work_listView.setOnItemClickListener { parent, view, position, id ->
                val intent = MonthWorkActivity.newIntent(this, worksheetList[position])
                startActivity(intent)
            }
        }
    }
}