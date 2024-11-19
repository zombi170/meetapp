package hu.bme.aut.android.meetapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import hu.bme.aut.android.meetapp.data.MeetupListDatabase
import hu.bme.aut.android.meetapp.databinding.FragmentChartBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChartFragment: Fragment() {

    private lateinit var binding: FragmentChartBinding
    private lateinit var database: MeetupListDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = MeetupListDatabase.getDatabase(requireContext())

        binding.chartDate.xAxis.setCenterAxisLabels(true)
        binding.chartDate.xAxis.setDrawGridLines(false)
        binding.chartDate.xAxis.setDrawAxisLine(false)
        binding.chartDate.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chartDate.xAxis.textColor = Color.WHITE

        binding.chartDate.axisLeft.setDrawGridLines(false)
        binding.chartDate.axisLeft.setDrawAxisLine(false)

        binding.chartDate.axisRight.setDrawGridLines(false)
        binding.chartDate.axisRight.setDrawAxisLine(false)
        binding.chartDate.axisRight.setDrawLabels(false)
        binding.chartDate.description.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        updateChart()
    }

    private fun updateChart() {
        lifecycleScope.launch {
            val dates = withContext(Dispatchers.IO) {
                database.meetupItemDao().getMeetupDates(activity?.intent?.extras?.getLong("meetupId")!!)
            }

            val dateAndChoiceCounts = mutableMapOf<String, Triple<Int, Int, Int>>()

            for (dateItem in dates) {
                if (dateAndChoiceCounts.containsKey(dateItem.date)) {
                    when(dateItem.choice) {
                        'Y' -> {
                            dateAndChoiceCounts[dateItem.date] = Triple(dateAndChoiceCounts[dateItem.date]!!.first + 1,
                                dateAndChoiceCounts[dateItem.date]!!.second,
                                dateAndChoiceCounts[dateItem.date]!!.third)
                        }
                        'M' -> {
                            dateAndChoiceCounts[dateItem.date] = Triple(dateAndChoiceCounts[dateItem.date]!!.first,
                                dateAndChoiceCounts[dateItem.date]!!.second + 1,
                                dateAndChoiceCounts[dateItem.date]!!.third)
                        }
                        'N' -> {
                            dateAndChoiceCounts[dateItem.date] = Triple(dateAndChoiceCounts[dateItem.date]!!.first,
                                dateAndChoiceCounts[dateItem.date]!!.second,
                                dateAndChoiceCounts[dateItem.date]!!.third + 1)
                        }
                    }
                } else {
                    when(dateItem.choice) {
                        'Y' -> {
                            dateAndChoiceCounts[dateItem.date] = Triple(1, 0, 0)
                        }
                        'M' -> {
                            dateAndChoiceCounts[dateItem.date] = Triple(0, 1, 0)
                        }
                        'N' -> {
                            dateAndChoiceCounts[dateItem.date] = Triple(0, 0, 1)
                        }
                    }
                }
            }

            val entries = mutableListOf<BarEntry>()
            val labels = mutableListOf<String>()
            var i = dateAndChoiceCounts.size.toFloat() - 1f

            for (date in dateAndChoiceCounts.keys) {
                labels.add(date)
                entries.add(BarEntry(i, floatArrayOf(dateAndChoiceCounts[date]!!.first.toFloat(),
                    dateAndChoiceCounts[date]!!.second.toFloat(),
                    dateAndChoiceCounts[date]!!.third.toFloat())))
                i -= 1f
            }

            val reversedLabels = labels.reversed()

            val dataSet = BarDataSet(entries, "Choices")
            dataSet.colors = intArrayOf(Color.GREEN, Color.YELLOW, Color.RED).toList()
            dataSet.stackLabels = arrayOf("Yes", "Maybe", "No")

            binding.chartDate.xAxis.valueFormatter = IndexAxisValueFormatter(reversedLabels)
            binding.chartDate.xAxis.labelCount = reversedLabels.size
            binding.chartDate.data = BarData(dataSet)
            binding.chartDate.invalidate()
        }
    }

}