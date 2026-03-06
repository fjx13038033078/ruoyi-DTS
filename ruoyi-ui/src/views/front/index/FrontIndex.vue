<template>
  <div class="front-index">
    <section class="banner-section">
      <div class="banner-content">
        <h1 class="banner-title">精彩演出 尽在掌握</h1>
        <p class="banner-subtitle">音乐剧票务管理系统 - 便捷购票，畅享视听盛宴</p>
      </div>
    </section>

    <section class="notice-section">
      <div class="section-header">
        <h2 class="section-title">
          <i class="el-icon-bell"></i>
          通知公告
        </h2>
      </div>
      <div class="notice-box" v-loading="noticeLoading">
        <template v-if="noticeList.length > 0">
          <ul class="notice-list">
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
        </template>
        <template v-else-if="!noticeLoading">
          <div class="notice-empty">
            <i class="el-icon-document"></i>
            <p>暂无公告</p>
          </div>
        </template>
      </div>
    </section>

    <section class="performance-section">
      <div class="section-header">
        <h2 class="section-title">
          <i class="el-icon-video-play"></i>
          热门演出
        </h2>
        <router-link to="/front/performance" class="view-all">查看全部</router-link>
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

    <el-dialog :title="selectedNotice.title" :visible.sync="showNoticeDialog" width="640px" append-to-body>
      <div v-html="selectedNotice.content" class="notice-content"></div>
    </el-dialog>
  </div>
</template>

<script>
import { parseTime } from '@/utils/ruoyi'
import { listPerformance } from '@/api/front/ticket'
import { getNotice, listNotice } from '@/api/system/notice'

export default {
  name: 'FrontIndex',
  dicts: ['sys_notice_type'],
  data() {
    return {
      loading: false,
      performanceList: [],
      noticeLoading: false,
      noticeList: [],
      selectedNotice: { title: '', content: '' },
      showNoticeDialog: false
    }
  },
  created() {
    this.loadPerformances()
    this.loadNoticeList()
  },
  methods: {
    loadPerformances() {
      this.loading = true
      listPerformance({
        pageNum: 1,
        pageSize: 12,
        status: '1',
        isRecommend: '1'
      }).then(res => {
        this.performanceList = res.rows || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    goDetail(performanceId) {
      this.$router.push({ path: '/front/detail/' + performanceId })
    },
    loadNoticeList() {
      this.noticeLoading = true
      listNotice({ pageNum: 1, pageSize: 8 }).then(res => {
        this.noticeList = res.rows || []
        this.noticeLoading = false
      }).catch(() => { this.noticeLoading = false })
    },
    showNoticeContent(row) {
      getNotice(row.noticeId).then(res => {
        this.selectedNotice.title = res.data.noticeTitle
        this.selectedNotice.content = res.data.noticeContent
        this.showNoticeDialog = true
      })
    },
    parseTime
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
    display: flex;
    align-items: center;
    justify-content: space-between;
    .section-title {
      font-size: 20px;
      color: #333;
      margin: 0;
      display: flex;
      align-items: center;
      gap: 8px;
    }
    .view-all {
      font-size: 14px;
      color: #3949ab;
      text-decoration: none;
      &:hover {
        text-decoration: underline;
      }
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

  .notice-section {
    margin-bottom: 30px;
  }

  .notice-box {
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
    padding: 16px;
  }

  .notice-list {
    list-style: none;
    margin: 0;
    padding: 0;
  }

  .notice-item {
    display: flex;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #f0f0f0;
    cursor: pointer;
    font-size: 14px;
    transition: color 0.2s ease;
    &:last-child {
      border-bottom: none;
    }
    &:hover {
      color: #3949ab;
    }
    .notice-title {
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .notice-time {
      color: #999;
      margin-left: 12px;
      flex-shrink: 0;
      font-size: 13px;
    }
  }

  .notice-empty {
    text-align: center;
    padding: 40px 20px;
    color: #999;
    i {
      font-size: 48px;
      display: block;
      margin-bottom: 12px;
    }
  }
}

.notice-content::v-deep img {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 0 auto;
}
</style>
