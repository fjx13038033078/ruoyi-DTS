<template>
  <div class="front-performance-list">
    <section class="section-header">
      <h2 class="section-title">
        <i class="el-icon-video-play"></i>
        全部演出
      </h2>
    </section>
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
                <div
                  v-if="isLogin"
                  class="card-favorite"
                  :class="{ favorited: favoritedIds.includes(item.performanceId) }"
                  @click.stop="toggleFavorite(item)"
                >
                  <i :class="favoritedIds.includes(item.performanceId) ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
                </div>
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
    <div class="pagination-wrap" v-show="total > 0 && performanceList.length > 0">
      <pagination
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </div>
  </div>
</template>

<script>
import { listPerformance, getMyFavoriteIds, addFavorite, removeFavorite } from '@/api/front/ticket'

export default {
  name: 'FrontPerformanceList',
  data() {
    return {
      loading: false,
      performanceList: [],
      favoritedIds: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 12,
        status: '1'
      }
    }
  },
  computed: {
    isLogin() {
      return this.$store.getters.token
    }
  },
  created() {
    this.getList()
    if (this.isLogin) this.loadFavoriteIds()
  },
  methods: {
    getList() {
      this.loading = true
      listPerformance(this.queryParams).then(res => {
        this.performanceList = res.rows || []
        this.total = res.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    goDetail(performanceId) {
      this.$router.push({ path: '/front/detail/' + performanceId })
    },
    loadFavoriteIds() {
      getMyFavoriteIds().then(res => {
        this.favoritedIds = res.data || []
      }).catch(() => {})
    },
    toggleFavorite(item) {
      if (!this.isLogin) return
      const isFav = this.favoritedIds.includes(item.performanceId)
      const api = isFav ? removeFavorite : addFavorite
      api(item.performanceId).then(() => {
        if (isFav) {
          this.favoritedIds = this.favoritedIds.filter(id => id !== item.performanceId)
        } else {
          this.favoritedIds = [...this.favoritedIds, item.performanceId]
        }
        this.$message.success(isFav ? '已取消收藏' : '收藏成功')
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.front-performance-list {
  .section-header {
    margin-bottom: 24px;
    .section-title {
      font-size: 22px;
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
      .card-favorite {
        position: absolute;
        top: 8px;
        left: 8px;
        width: 32px;
        height: 32px;
        border-radius: 50%;
        background: rgba(0, 0, 0, 0.4);
        color: #fff;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        font-size: 18px;
        &:hover { background: rgba(0, 0, 0, 0.6); }
        &.favorited { color: #ffc107; }
      }
    }
    .card-info {
      padding: 16px;
      .card-title {
        font-size: 16px;
        margin: 0;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
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

  .pagination-wrap {
    margin-top: 24px;
    padding: 16px 0;
    ::v-deep .pagination-container {
      background: transparent;
      padding: 0;
    }
  }
}
</style>
