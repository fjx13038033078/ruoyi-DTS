<template>
  <div class="dashboard-container">
    <div class="page-header">
      <h1 class="page-title">音乐剧票务管理平台</h1>
      <p class="page-subtitle">数据概览与运营分析</p>
    </div>

    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <div class="stat-card stat-card--ticket">
          <div class="stat-label">售票量</div>
          <div class="stat-value">{{ summary.ticketCount || 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card--amount">
          <div class="stat-label">销售额（元）</div>
          <div class="stat-value">{{ summary.totalAmount || 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card--occupancy">
          <div class="stat-label">上座率</div>
          <div class="stat-value">{{ summary.occupancyRate || 0 }}%</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card--order">
          <div class="stat-label">订单数</div>
          <div class="stat-value">{{ summary.orderCount || 0 }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <div class="chart-box">
          <div class="chart-header">
            <span>销售趋势</span>
            <el-radio-group v-model="salesPeriod" size="mini" @change="loadSalesStats">
              <el-radio-button label="day">日</el-radio-button>
              <el-radio-button label="week">周</el-radio-button>
              <el-radio-button label="month">月</el-radio-button>
            </el-radio-group>
          </div>
          <div ref="lineChart" class="chart-body"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-box">
          <div class="chart-header">
            <span>销售额趋势</span>
          </div>
          <div ref="barChart" class="chart-body"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <div class="chart-box">
          <div class="chart-header">
            <span>热门座位区域</span>
          </div>
          <div ref="pieChart" class="chart-body"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="notice-box">
          <div class="notice-header">通知公告</div>
          <ul class="notice-list" v-loading="loading">
            <li
              v-for="item in noticeList"
              :key="item.noticeId"
              class="notice-item"
              @click="showNoticeContent(item)"
            >
              <span class="notice-title">{{ item.noticeTitle }}</span>
              <dict-tag :options="dict.type.sys_notice_type" :value="item.noticeType" />
              <span class="notice-time">{{ parseTime(item.createTime, '{m}-{d}') }}</span>
            </li>
          </ul>
          <div v-if="!loading && noticeList.length === 0" class="notice-empty">暂无公告</div>
        </div>
      </el-col>
    </el-row>

    <el-dialog :title="selectedNotice.title" :visible.sync="showNoticeDialog" width="640px" append-to-body>
      <div v-html="selectedNotice.content" class="notice-content"></div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { parseTime } from '@/utils/ruoyi'
import { getNotice, listNotice } from '@/api/system/notice'
import { getSalesStats, getAreaStats, getStatsSummary } from '@/api/ticket/stats'

export default {
  name: 'Index',
  dicts: ['sys_notice_type'],
  data() {
    return {
      loading: true,
      noticeList: [],
      selectedNotice: { title: '', content: '' },
      showNoticeDialog: false,
      queryParams: { pageNum: 1, pageSize: 8 },
      summary: {},
      salesPeriod: 'day',
      salesData: [],
      areaData: [],
      lineChart: null,
      barChart: null,
      pieChart: null
    }
  },
  created() {
    this.getNoticeList()
    this.loadSummary()
    this.loadSalesStats()
    this.loadAreaStats()
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.lineChart && this.lineChart.dispose()
    this.barChart && this.barChart.dispose()
    this.pieChart && this.pieChart.dispose()
  },
  methods: {
    parseTime,
    getNoticeList() {
      this.loading = true
      listNotice(this.queryParams).then(res => {
        this.noticeList = res.rows || []
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    showNoticeContent(row) {
      getNotice(row.noticeId).then(res => {
        this.selectedNotice.title = res.data.noticeTitle
        this.selectedNotice.content = res.data.noticeContent
        this.showNoticeDialog = true
      })
    },
    loadSummary() {
      getStatsSummary().then(res => {
        this.summary = res.data || {}
      }).catch(() => {})
    },
    loadSalesStats() {
      getSalesStats(this.salesPeriod).then(res => {
        this.salesData = res.data || {}
        this.updateLineChart()
        this.updateBarChart()
      }).catch(() => {})
    },
    loadAreaStats() {
      getAreaStats().then(res => {
        this.areaData = res.data || []
        this.updatePieChart()
      }).catch(() => {})
    },
    initCharts() {
      this.lineChart = echarts.init(this.$refs.lineChart)
      this.barChart = echarts.init(this.$refs.barChart)
      this.pieChart = echarts.init(this.$refs.pieChart)
      this.updateLineChart()
      this.updateBarChart()
      this.updatePieChart()
      window.addEventListener('resize', this.handleResize)
    },
    handleResize() {
      this.lineChart && this.lineChart.resize()
      this.barChart && this.barChart.resize()
      this.pieChart && this.pieChart.resize()
    },
    updateLineChart() {
      if (!this.lineChart) return
      const list = this.salesData.list || []
      const dates = list.map(d => d.date)
      const counts = list.map(d => d.count || 0)
      this.lineChart.setOption({
        color: ['#2c5aa0'],
        grid: { left: 40, right: 20, top: 20, bottom: 30 },
        xAxis: { type: 'category', data: dates, axisLine: { lineStyle: { color: '#ddd' } } },
        yAxis: { type: 'value', axisLine: { show: false }, splitLine: { lineStyle: { color: '#eee' } } },
        tooltip: { trigger: 'axis' },
        series: [{ type: 'line', data: counts, smooth: true, symbol: 'circle', symbolSize: 6 }]
      })
    },
    updateBarChart() {
      if (!this.barChart) return
      const list = this.salesData.list || []
      const dates = list.map(d => d.date)
      const amounts = list.map(d => parseFloat(d.amount) || 0)
      this.barChart.setOption({
        color: ['#2d7a4f'],
        grid: { left: 50, right: 20, top: 20, bottom: 30 },
        xAxis: { type: 'category', data: dates, axisLine: { lineStyle: { color: '#ddd' } } },
        yAxis: { type: 'value', axisLine: { show: false }, splitLine: { lineStyle: { color: '#eee' } } },
        tooltip: { trigger: 'axis', formatter: '{b}: ¥{c}' },
        series: [{ type: 'bar', data: amounts, barWidth: '50%' }]
      })
    },
    updatePieChart() {
      if (!this.pieChart) return
      const data = this.areaData.map(d => ({ name: d.name, value: d.value }))
      if (data.length === 0) data.push({ name: '暂无数据', value: 1 })
      this.pieChart.setOption({
        color: ['#2c5aa0', '#2d7a4f', '#b45309', '#4a5568', '#5a6c7d'],
        tooltip: { trigger: 'item' },
        legend: { bottom: 10, left: 'center' },
        series: [{
          type: 'pie',
          radius: '60%',
          center: ['50%', '45%'],
          data,
          label: { fontSize: 12 }
        }]
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 0;
}

.page-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid #2c5aa0;
  border-left: 4px solid #2c5aa0;
  padding-left: 16px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #1a365d;
  margin: 0 0 6px 0;
  letter-spacing: 0.5px;
}

.page-subtitle {
  font-size: 13px;
  color: #5a6c7d;
  margin: 0;
}

.stats-row {
  margin-bottom: 16px;
}

.stat-card {
  padding: 18px;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-left-width: 4px;
  .stat-label {
    font-size: 13px;
    color: #5a6c7d;
    margin-bottom: 8px;
    font-weight: 500;
  }
  .stat-value {
    font-size: 26px;
    font-weight: 600;
    color: #1a365d;
  }
  &--ticket { border-left-color: #2c5aa0; }
  &--amount { border-left-color: #2d7a4f; }
  &--occupancy { border-left-color: #b45309; }
  &--order { border-left-color: #4a5568; }
}

.chart-row {
  margin-bottom: 16px;
}

.chart-box {
  background: #fff;
  border: 1px solid #e0e0e0;
  border-top: 3px solid #2c5aa0;
  padding: 16px;
  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    font-size: 15px;
    font-weight: 600;
    color: #1a365d;
  }
  .chart-body {
    height: 260px;
  }
}

.notice-box {
  background: #fff;
  border: 1px solid #e0e0e0;
  border-top: 3px solid #4a5568;
  padding: 16px;
  .notice-header {
    font-size: 15px;
    font-weight: 600;
    color: #1a365d;
    margin-bottom: 12px;
  }
  .notice-list {
    list-style: none;
    margin: 0;
    padding: 0;
  }
  .notice-item {
    display: flex;
    align-items: center;
    padding: 10px 0;
    border-bottom: 1px solid #f0f0f0;
    cursor: pointer;
    font-size: 13px;
    &:last-child { border-bottom: none; }
    &:hover { color: #2c5aa0; }
    .notice-title {
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .notice-time {
      color: #999;
      margin-left: 8px;
      flex-shrink: 0;
    }
  }
  .notice-empty {
    color: #999;
    text-align: center;
    padding: 40px 0;
  }
}

.notice-content::v-deep img {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 0 auto;
}
</style>
