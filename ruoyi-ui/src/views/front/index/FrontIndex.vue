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
            >
              <div class="card-poster">
                <image-preview v-if="item.posterUrl" :src="item.posterUrl" :width="200" :height="140" />
                <div v-else class="poster-placeholder">
                  <i class="el-icon-video-play"></i>
                  <span>暂无海报</span>
                </div>
              </div>
              <div class="card-info">
                <h3 class="card-title">{{ item.title }}</h3>
                <div class="card-meta">
                  <el-tag v-if="item.isRecommend === '1'" type="danger" size="mini">推荐</el-tag>
                  <el-tag :type="item.status === '1' ? 'success' : 'info'" size="mini">
                    {{ item.status === '1' ? '上架' : '下架' }}
                  </el-tag>
                </div>
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
import { listPerformance } from '@/api/ticket/performance'

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
        pageSize: 8,
        status: '1',
        isRecommend: undefined
      }).then(res => {
        this.performanceList = res.rows || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
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
    grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
    gap: 20px;
  }

  .performance-card {
    background: #fff;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    transition: transform 0.2s;
    &:hover {
      transform: translateY(-4px);
    }
    .card-poster {
      height: 140px;
      background: #f0f0f0;
      display: flex;
      align-items: center;
      justify-content: center;
      .poster-placeholder {
        color: #999;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 8px;
        i {
          font-size: 40px;
        }
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
