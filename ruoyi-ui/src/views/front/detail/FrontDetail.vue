<template>
  <div class="front-detail">
    <div v-loading="loading" class="detail-content">
      <template v-if="performance">
        <section class="detail-top">
          <div class="poster-wrap">
            <image-preview v-if="performance.posterUrl" :src="performance.posterUrl" :width="280" :height="380" />
            <div v-else class="poster-placeholder">
              <i class="el-icon-video-play"></i>
              <span>暂无海报</span>
            </div>
          </div>
          <div class="info-wrap">
            <h1 class="title">{{ performance.title }}</h1>
            <el-tag v-if="performance.isRecommend === '1'" type="danger" size="small">推荐</el-tag>
            <div v-if="performance.description" class="description">
              <h3>剧目简介</h3>
              <p>{{ performance.description }}</p>
            </div>
          </div>
        </section>

        <section class="session-section">
          <h2 class="section-title">
            <i class="el-icon-date"></i>
            选择场次
          </h2>
          <div class="session-buttons">
            <el-button
              v-for="s in sessions"
              :key="s.sessionId"
              :type="selectedSession && selectedSession.sessionId === s.sessionId ? 'primary' : 'default'"
              class="session-btn"
              @click="selectSession(s)"
            >
              <div class="session-info">
                <span class="venue">{{ s.venueName }}</span>
                <span class="time">{{ parseTime(s.startTime, '{y}-{m}-{d} {h}:{i}') }}</span>
                <el-tag :type="s.status === '1' ? 'success' : 'info'" size="mini">
                  {{ s.status === '1' ? '售票中' : s.status === '0' ? '未开始' : '已结束' }}
                </el-tag>
              </div>
            </el-button>
          </div>
          <div v-if="selectedSession && selectedSession.status === '1'" class="action-row">
            <el-button type="primary" size="medium" @click="goSeatSelect">
              选座购票 <i class="el-icon-arrow-right"></i>
            </el-button>
          </div>
        </section>
      </template>
    </div>
  </div>
</template>

<script>
import { getPerformanceDetail } from '@/api/front/ticket'

export default {
  name: 'FrontDetail',
  data() {
    return {
      loading: false,
      performanceId: null,
      performance: null,
      sessions: [],
      selectedSession: null
    }
  },
  created() {
    this.performanceId = this.$route.params.id
    this.loadDetail()
  },
  methods: {
    loadDetail() {
      if (!this.performanceId) return
      this.loading = true
      getPerformanceDetail(this.performanceId).then(res => {
        this.performance = res.data.performance
        this.sessions = res.data.sessions || []
        this.selectedSession = this.sessions.find(s => s.status === '1') || this.sessions[0] || null
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    selectSession(session) {
      this.selectedSession = session
    },
    goSeatSelect() {
      if (!this.selectedSession) return
      this.$router.push({ path: '/front/seat/' + this.selectedSession.sessionId })
    }
  }
}
</script>

<style lang="scss" scoped>
.front-detail {
  .detail-content {
    background: #fff;
    border-radius: 12px;
    padding: 32px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  }

  .detail-top {
    display: flex;
    gap: 40px;
    margin-bottom: 40px;
  }

  .poster-wrap {
    flex-shrink: 0;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
    .poster-placeholder {
      width: 280px;
      height: 380px;
      background: #e8eaf6;
      color: #9fa8da;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 12px;
      i { font-size: 64px; }
    }
  }

  .info-wrap {
    flex: 1;
    .title {
      font-size: 28px;
      margin: 0 0 16px 0;
      color: #1a237e;
    }
    .description {
      margin-top: 24px;
      h3 {
        font-size: 16px;
        margin: 0 0 12px 0;
        color: #333;
      }
      p {
        margin: 0;
        line-height: 1.8;
        color: #666;
        white-space: pre-wrap;
      }
    }
  }

  .session-section {
    .section-title {
      font-size: 18px;
      margin: 0 0 20px 0;
      color: #333;
      display: flex;
      align-items: center;
      gap: 8px;
    }
    .session-buttons {
      display: flex;
      flex-wrap: wrap;
      gap: 12px;
    }
    .session-btn {
      min-width: 200px;
      .session-info {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        gap: 4px;
        .venue { font-weight: 500; }
        .time { font-size: 13px; color: #666; }
      }
    }
    .action-row {
      margin-top: 24px;
    }
  }
}
</style>
