<template>
  <div class="front-index">
    <section class="banner-section">
      <div class="banner-content">
        <h1 class="banner-title">精彩演出 尽在掌握</h1>
        <p class="banner-subtitle">音乐剧票务管理系统 - 便捷购票，畅享视听盛宴</p>
      </div>
    </section>

    <section class="performance-section">
      <div class="section-header">
        <h2 class="section-title">
          <i class="el-icon-video-play"></i>
          热门演出
        </h2>
      </div>
      <div class="performance-list" v-loading="loading">
        <template v-if="performanceList.length > 0">
          <div class="performance-grid">
            <div
              v-for="item in performanceList"
              :key="item.performanceId"
              class="performance-card"
              @click="goDetail(item.performanceId)"
            >
              <div class="card-poster">
                <image-preview v-if="item.posterUrl" :src="item.posterUrl" :width="220" :height="160" />
                <div v-else class="poster-placeholder">
                  <i class="el-icon-video-play"></i>
                  <span>暂无海报</span>
                </div>
                <div v-if="item.isRecommend === '1'" class="card-badge">推荐</div>
              </div>
              <div class="card-info">
                <h3 class="card-title">{{ item.title }}</h3>
              </div>
            </div>
          </div>
        </template>
        <template v-else-if="!loading">
          <div class="empty-tip">
            <i class="el-icon-document"></i>
            <p>暂无演出信息</p>
          </div>
        </template>
      </div>
    </section>
  </div>
</template>

<script>
import { listPerformance } from '@/api/front/ticket'

export default {
  name: 'FrontIndex',
  data() {
    return {
      loading: false,
      performanceList: []
    }
  },
  created() {
    this.loadPerformances()
  },
  methods: {
    loadPerformances() {
      this.loading = true
      listPerformance({
        pageNum: 1,
        pageSize: 12,
        status: '1'
      }).then(res => {
        this.performanceList = res.rows || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    goDetail(performanceId) {
      this.$router.push({ path: '/front/detail/' + performanceId })
    }
  }
}
</script>

<style lang="scss" scoped>
.front-index {
  .banner-section {
    background: linear-gradient(135deg, #3949ab 0%, #5c6bc0 100%);
    border-radius: 8px;
    padding: 40px;
    margin-bottom: 30px;
    text-align: center;
    .banner-title {
      color: #fff;
      font-size: 28px;
      margin: 0 0 12px 0;
    }
    .banner-subtitle {
      color: rgba(255, 255, 255, 0.9);
      font-size: 16px;
      margin: 0;
    }
  }

  .section-header {
    margin-bottom: 20px;
    .section-title {
      font-size: 20px;
      color: #333;
      margin: 0;
      display: flex;
      align-items: center;
      gap: 8px;
    }
  }

  .performance-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 24px;
  }

  .performance-card {
    background: #fff;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;
    cursor: pointer;
    &:hover {
      transform: translateY(-6px);
      box-shadow: 0 8px 24px rgba(57, 73, 171, 0.2);
    }
    .card-poster {
      position: relative;
      height: 160px;
      background: #e8eaf6;
      display: flex;
      align-items: center;
      justify-content: center;
      overflow: hidden;
      .poster-placeholder {
        color: #9fa8da;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 8px;
        i {
          font-size: 48px;
        }
      }
      .card-badge {
        position: absolute;
        top: 8px;
        right: 8px;
        background: #f44336;
        color: #fff;
        font-size: 12px;
        padding: 2px 8px;
        border-radius: 4px;
      }
    }
    .card-info {
      padding: 16px;
      .card-title {
        font-size: 16px;
        margin: 0 0 8px 0;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      .card-meta {
        display: flex;
        gap: 8px;
      }
    }
  }

  .empty-tip {
    text-align: center;
    padding: 60px 20px;
    color: #999;
    i {
      font-size: 48px;
      display: block;
      margin-bottom: 12px;
    }
  }
}
</style>
