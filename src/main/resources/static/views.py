# Create your views here.
import datetime
import hashlib

from django.db.models import Q, Count
from django.db.models import F
from app01 import models
from django.http import HttpResponse, JsonResponse
import json
from dateutil.relativedelta import relativedelta
from django.core import serializers
from django.forms.models import model_to_dict

def register(request):
    response={'code':0,'msg':'success'}
    data = {}
    if request.method=='GET':
        college_list = models.College.objects.all()
        if request.GET.get('college_id') != '0':
            major_list = models.Major.objects.filter(college_id=request.GET.get('college_id'))
            m_list = {}
            for m in major_list:
                m_list[str(m.id)] = {}
                m_list[str(m.id)]['name'] = m.name
            data['major_list'] = m_list
        if request.GET.get('major_id') != '0':
            class_list = models.Class.objects.filter(major_id=request.GET.get('major_id'))
            c_list = {}
            for c in class_list:
                c_list[str(c.id)] = {}
                c_list[str(c.id)]['name'] = c.name
            data['class_list'] = c_list
        c_list = {}
        for c in college_list:
            c_list[str(c.id)] = {}
            c_list[str(c.id)]['name'] = c.name
        data['college_list'] = c_list
        response['data'] = data
    if request.method=='POST':
        body=str(request.body,encoding='utf-8')
        print(body)
        try:
            info = json.loads(body)# 解析json报文
        except:
            response['code'] = '-1'
            response['msg'] = '请求格式有误'
        if models.User.objects.filter(sid=info.get('id')).first():
            response['error_num'] = 1
            response['msg'] = '用户已存在'
        else:
            user = models.User.objects.create(**info)
            response['msg'] = 'success'
            response['error_num'] = 0
    return HttpResponse(json.dumps(response))

def login(request):
    response={'code':'0','msg':'success'}
    data = {}
    if request.method=='POST':
        body=str(request.body,encoding='utf-8')
        print(body)
        try:
            info = json.loads(body)#解析json报文
        except:
            response['code'] = '-1'
            response['msg'] = '请求格式有误'
        user = models.User.objects.filter(sid=info.get('sid'),psw=info.get('psw')).first()
        if user:
            response['data'] = data
            response['msg'] = 'success'
            data['sid'] = info.get('sid')
            data['name'] = user.name
            data['avatar'] = user.avatar
            data['psw'] = user.psw
            data['is_society'] = user.is_society
            data['is_support'] = user.is_support
            data['total_don_time'] = user.total_don_time
            if not user.is_society:
                data['college'] = user.college.name
                data['major'] = user.major.name
                data['classs'] = user.classs.name
        else:
            response['msg'] = '登陆失败'
            response['code'] = '-1'
    return HttpResponse(json.dumps(response))

def select_vol(request):
    response = {'code': 0, 'msg': 'success'}
    data = {}
    if request.method == 'GET':
        # 每次查询时，检查状态
        vol_list = models.Volunteering.objects.exclude(creater_id="18996343508")
        v_list = {}
        for i in vol_list:
            if i.state != 2:
                if datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S") < i.start_time.strftime("%Y-%m-%d %H:%M:%S"):
                    i.state = 0
                elif datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S") < i.end_time.strftime("%Y-%m-%d %H:%M:%S"):
                    i.state = 1
                else:
                    i.real_end_time = i.end_time.strftime("%Y-%m-%d %H:%M:%S")
                    i.state = 2
                i.save()
            v_list[str(i.id)] = {}
            v_list[str(i.id)]['name'] = i.name
            v_list[str(i.id)]['state'] = i.state
            v_list[str(i.id)]['time'] = i.time
            v_list[str(i.id)]['su_start_time'] = i.su_start_time.strftime("%Y-%m-%d %H:%M:%S")
            v_list[str(i.id)]['su_end_time'] = i.su_end_time.strftime("%Y-%m-%d %H:%M:%S")
            v_list[str(i.id)]['start_time'] = i.start_time.strftime("%Y-%m-%d %H:%M:%S")
            v_list[str(i.id)]['end_time'] = i.end_time.strftime("%Y-%m-%d %H:%M:%S")
            v_list[str(i.id)]['address'] = i.address
            v_list[str(i.id)]['content'] = i.content
            v_list[str(i.id)]['phone'] = i.phone
            v_list[str(i.id)]['creater_name'] = i.creater.name
        data['vol_list'] = v_list
    if request.method == 'POST':
        body = str(request.body, encoding='utf-8')
        print(body)
        try:
            info = json.loads(body)  # 解析json报文
        except:
            response['code'] = '-1'
            response['msg'] = '请求格式有误'
        vol = models.Volunteering.objects.filter(id=int(info.get('id'))).first()
        user = models.User.objects.filter(sid=info.get("sid")).first()
        apply = models.Apply.objects.filter(volunteering=vol,user=user).first()
        data['is_apply'] = False
        if apply or vol.creater == user or vol.state == 2:
            data['is_apply'] = True
    #     if vol:
    #         response['error_num'] = 0
    #         response['msg'] = 'success'
    #         response['creater_name'] = models.User.objects.filter(user_to_vol__volunteering_id="001").first().name
    response['data'] = data
    return HttpResponse(json.dumps(response))

def search_vol(request):
    response = {'code': 0, 'msg': 'success'}
    data = {}
    # 关键字查询,状态查询
    if request.method == 'GET':
        vol_list = models.Volunteering.objects.exclude(creater_id='18996343508').filter(Q(name__contains=request.GET.get('keyword'))).reverse()
        if request.GET.get('state') != '3' and request.GET.get('state'):
            vol_list = vol_list.filter(state=request.GET.get('state'))
        v_list = {}
        for i in vol_list:
            v_list[str(i.id)] = {}
            v_list[str(i.id)]['name'] = i.name
            v_list[str(i.id)]['state'] = i.state
            v_list[str(i.id)]['time'] = i.time
            v_list[str(i.id)]['su_start_time'] = i.su_start_time.strftime("%Y-%m-%d %H:%M:%S")
            v_list[str(i.id)]['su_end_time'] = i.su_end_time.strftime("%Y-%m-%d %H:%M:%S")
            v_list[str(i.id)]['start_time'] = i.start_time.strftime("%Y-%m-%d %H:%M:%S")
            v_list[str(i.id)]['end_time'] = i.end_time.strftime("%Y-%m-%d %H:%M:%S")
            v_list[str(i.id)]['address'] = i.address
            v_list[str(i.id)]['content'] = i.content
            v_list[str(i.id)]['phone'] = i.phone
            v_list[str(i.id)]['creater_name'] = i.creater.name
        # 时间段筛选
        # start_time = info.get('start_time')
        # end_time = info.get('end_time')
        # for n in range(start_time, end_time):
        #     needed_time = (datetime.datetime.today() - relativedelta(months=n)).strftime("%Y-%m")
        #     for i in v_list:
        #         if i.start_time.strftime("%Y-%m") == needed_time:
        #             vol_list.append(i)
        data['vol_list'] = v_list
        response['data'] = data
    return HttpResponse(json.dumps(response))

def post_vol(request):
    # get判断是否有资质
    response = {'code': 0, 'msg': 'success'}
    data = {}
    if request.method == 'GET':
        user = models.User.objects.filter(sid=request.GET.get('id')).first()
        data['is_post'] = False
        vol = models.Volunteering.objects.filter(creater=user).exclude(state=2).first()
        # 发布过的志愿活动状态是否结束
        if vol and vol.state != 2:
            data['is_post'] = True
            data['vol'] = {}
            data['vol']['id'] = vol.id
            data['vol']['time'] = vol.time
            data['vol']['state'] = vol.state
            data['vol']['name'] = vol.name
            data['vol']['su_start_time'] = vol.su_start_time.strftime("%Y-%m-%d %H:%M:%S")
            data['vol']['su_end_time'] = vol.su_end_time.strftime("%Y-%m-%d %H:%M:%S")
            data['vol']['su_end_time'] = vol.su_end_time.strftime("%Y-%m-%d %H:%M:%S")
            data['vol']['start_time'] = vol.start_time.strftime("%Y-%m-%d %H:%M:%S")
            data['vol']['end_time'] = vol.end_time.strftime("%Y-%m-%d %H:%M:%S")
            data['vol']['address'] = vol.address
            data['vol']['content'] = vol.content
            data['vol']['phone'] = vol.phone
            data['vol']['creater_name'] = vol.creater.name
        data['is_support'] =  user.is_support
        response['data'] = data
    # post接受存储信息
    if request.method == 'POST':
        body = str(request.body, encoding='utf-8')
        print(body)
        try:
            info = json.loads(body)  # 解析json报文
        except:
            response['code'] = '-1'
            response['msg'] = '请求格式有误'
        # info -- name\su_start_time\su_end_time\start_time\end_time\address\content\time
        # id\state
        # 时间戳 + creater_id 的hash后八位
        # m = hashlib.md5()
        # s = datetime.datetime.today().strftime("%Y-%m-%d %H:%M:%S") + info.get('creater_id')
        # m.update(s.encode('utf-8'))
        start_time = info.get('start_time').replace('T',' ')[:-5]
        su_start_time = info.get('su_start_time').replace('T',' ')[:-5]
        end_time = info.get('end_time').replace('T',' ')[:-5]
        su_end_time = info.get('su_end_time').replace('T',' ')[:-5]
        info['start_time'] = (datetime.datetime.strptime(start_time,"%Y-%m-%d %H:%M:%S") + relativedelta(hours=8)).strftime("%Y-%m-%d %H:%M:%S")
        info['su_start_time'] = (datetime.datetime.strptime(su_start_time,"%Y-%m-%d %H:%M:%S") + relativedelta(hours=31)).strftime("%Y-%m-%d %H:%M:%S")
        info['end_time'] = (datetime.datetime.strptime(end_time,"%Y-%m-%d %H:%M:%S") + relativedelta(hours=8)).strftime("%Y-%m-%d %H:%M:%S")
        info['su_end_time'] = (datetime.datetime.strptime(su_end_time,"%Y-%m-%d %H:%M:%S") + relativedelta(hours=31)).strftime("%Y-%m-%d %H:%M:%S")
        info['real_end_time'] = info.get('end_time')
        if datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S") < info.get('start_time'):
            info['state'] = 0
        elif datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S") < info.get('end_time'):
            info['state'] = 1
        else:
            info['state'] = 2
        # 创建时长记录
        # info['state'] = 1
        vol = models.Volunteering.objects.create(**info)
        models.Donated_time.objects.create(length=info.get('time'),volunteering=vol)

        response['id'] = vol.id
    return HttpResponse(json.dumps(response))

def poster_backend(request):
    # 展示报名信息，状态
    response = {'code': 0, 'msg': 'success'}
    data = {}
    if request.method == 'GET':
        # 刷新纪录
        # 报名时间结束，申请自动拒绝
        a_list = models.Apply.objects.all()
        for a in a_list:
            v = models.Volunteering.objects.filter(apply=a).first()
            if datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S") > v.su_end_time.strftime("%Y-%m-%d %H:%M:%S") and a.state == 0:
                a.state = 2
            a.save()
        vol = models.Volunteering.objects.filter(creater_id=request.GET.get('id')).exclude(state=2).reverse().first()
        if vol:
            apl_list = models.Apply.objects.filter(volunteering=vol)
            data['vol'] = {}
            data['vol']['id'] = vol.id
            data['vol']['name'] = vol.name
            data['vol']['su_start_time'] = vol.su_start_time.strftime("%Y-%m-%d %H:%M:%S")
            data['vol']['su_end_time'] = vol.su_end_time.strftime("%Y-%m-%d %H:%M:%S")
            data['vol']['start_time'] = vol.start_time.strftime("%Y-%m-%d %H:%M:%S")
            data['vol']['end_time'] = vol.end_time.strftime("%Y-%m-%d %H:%M:%S")
            data['vol']['creater_name'] = vol.creater.name
            data['apply_list'] = {}
            for i in apl_list:
                data['apply_list'][str(i.id)] = {}
                data['apply_list'][str(i.id)]['user_name'] = i.user.name
                data['apply_list'][str(i.id)]['content'] = i.content
                data['apply_list'][str(i.id)]['user_id'] = i.user.sid
                data['apply_list'][str(i.id)]['user_college'] = i.user.college.name
                data['apply_list'][str(i.id)]['state'] = i.state

    if request.method == 'POST':
        body = str(request.body, encoding='utf-8')
        print(body)
        try:
            info = json.loads(body)  # 解析json报文
        except:
            response['code'] = '-1'
            response['msg'] = '请求格式有误'
        apply = models.Apply.objects.filter(id=info.get('apl_id'))
        apply.update(state=info.get('state'))
        if info.get('state') == 1:
            models.User_to_Vol.objects.create(user=apply.first().user,volunteering=apply.first().volunteering)
    response['data'] = data
    return HttpResponse(json.dumps(response))

def show_comment(request):
    # get展示按月份分，到着来
    response = {'code': 0, 'msg': 'success'}
    data = {}
    if request.method == 'GET':
        needed_time = (datetime.datetime.now() - relativedelta(months=int(request.GET.get('month_num')))).strftime("%Y-%m")
        v_list = models.Volunteering.objects.all()
        data['vol_list'] = {}
        for i in v_list:
            if i.real_end_time.strftime("%Y-%m") == needed_time and i.picture1:
                data['vol_list'][str(i.id)] = {}
                data['vol_list'][str(i.id)]['name'] = i.name
                data['vol_list'][str(i.id)]['picture1'] = i.picture1
                data['vol_list'][str(i.id)]['end_time'] = i.real_end_time.strftime("%Y-%m-%d %H:%M:%S")
    # post展示具体活动评论
    if request.method == 'POST':
        body = str(request.body, encoding='utf-8')
        print(body)
        try:
            info = json.loads(body)  # 解析json报文
        except:
            response['code'] = '-1'
            response['msg'] = '请求格式有误'
        vol = models.Volunteering.objects.filter(id=int(info.get('id'))).first()
        data['name'] = vol.name
        data['picture1'] = vol.picture1
        data['picture2'] = vol.picture2
        data['picture3'] = vol.picture3
        data['picture4'] = vol.picture4
        data['tag_1'] = vol.tag_1
        data['tag_2'] = vol.tag_2
        data['tag_3'] = vol.tag_3

        comment_list = models.Comment.objects.filter(volunteering=vol)
        data['comment_list'] = {}
        print(comment_list)
        for c in comment_list:
            data['comment_list'][str(c.id)] = {}
            data['comment_list'][str(c.id)]['user_name'] = c.user.name
            data['comment_list'][str(c.id)]['content'] = c.content

    response['data'] = data
    return HttpResponse(json.dumps(response))

def user_site(request):
    # 返回用户数据
    response = {'code': 0, 'msg': 'success'}
    data = {}
    if request.method == 'GET':
        user = models.User.objects.filter(sid=request.GET.get('id')).first()
        print(user)
        apl_list = models.Apply.objects.filter(user=user)
        post_list = models.Volunteering.objects.filter(creater=user)
        data['userinfo'] = {}
        data['apply_list'] = {}
        data['post_list'] = {}
        data['userinfo']['avatar'] = user.avatar
        data['userinfo']['total_don_time'] = user.total_don_time
        data['userinfo']['is_support'] = user.is_support
        data['userinfo']['is_society'] = user.is_society
        # 如果是社会人不需要班级专业等信息
        if not user.is_society:
            data['userinfo']['college'] = user.college.name
            data['userinfo']['major'] = user.major.name
            data['userinfo']['calsss'] = user.classs.name
        #
        for i in apl_list:
            vol = models.Volunteering.objects.filter(apply=i).first()
            comment = models.Comment.objects.filter(volunteering=vol,user=user)
            data['apply_list'][str(i.id)] = {}
            data['apply_list'][str(i.id)]['name'] = vol.name
            data['apply_list'][str(i.id)]['content'] = vol.content
            data['apply_list'][str(i.id)]['apl_state'] = i.state
            data['apply_list'][str(i.id)]['time'] = vol.time
            data['apply_list'][str(i.id)]['address'] = vol.address
            data['apply_list'][str(i.id)]['content'] = vol.content
            data['apply_list'][str(i.id)]['id'] = vol.id
            data['apply_list'][str(i.id)]['start_time'] = vol.start_time.strftime("%Y-%m-%d %H:%M:%S")
            data['apply_list'][str(i.id)]['end_time'] = vol.end_time.strftime("%Y-%m-%d %H:%M:%S")
            data['apply_list'][str(i.id)]['su_start_time'] = vol.su_start_time.strftime("%Y-%m-%d %H:%M:%S")
            data['apply_list'][str(i.id)]['su_end_time'] = vol.su_end_time.strftime("%Y-%m-%d %H:%M:%S")
            data['apply_list'][str(i.id)]['state'] = vol.state
            data['apply_list'][str(i.id)]['is_comment'] = False
            data['apply_list'][str(i.id)]['creater_name'] = vol.creater.name
            data['apply_list'][str(i.id)]['phone'] = vol.phone
            if comment:
                data['apply_list'][str(i.id)]['is_comment'] = True
        # 查询发布过的志愿信息，java重现的时候还是要先判断下是否有发布资质
        for i in post_list:
            data['post_list'][str(i.id)] = {}
            data['post_list'][str(i.id)]['vol_name'] = i.name
            data['post_list'][str(i.id)]['vol_id'] = i.id
            data['post_list'][str(i.id)]['su_start_time'] = i.su_start_time.strftime("%Y-%m-%d %H:%M:%S")
            data['post_list'][str(i.id)]['su_end_time'] = i.su_end_time.strftime("%Y-%m-%d %H:%M:%S")
            data['post_list'][str(i.id)]['vol_start_time'] = i.start_time.strftime("%Y-%m-%d %H:%M:%S")
            data['post_list'][str(i.id)]['vol_end_time'] = i.end_time.strftime("%Y-%m-%d %H:%M:%S")
            data['post_list'][str(i.id)]['vol_state'] = i.state
    response['data'] = data
    return HttpResponse(json.dumps(response))

def comment(request):
    response = {'code': 0, 'msg': 'success'}
    if request.method == 'POST':
        body = str(request.body, encoding='utf-8')
        print(body)
        try:
            info = json.loads(body)  # 解析json报文
        except:
            response['code'] = '-1'
            response['msg'] = '请求格式有误'
        vol = models.Volunteering.objects.filter(id=info.get('vol_id'))
        # tag数增加
        if info.get('tag_id') == '1':
            vol.update(tag_1=F('tag_1') + 1)
        if info.get('tag_id') == '2':
            vol.update(tag_2=F('tag_2') + 1)
        if info.get('tag_id') == '3':
            vol.update(tag_3=F('tag_3') + 1)
        vol.first().save()
        # 评论添加至数据库,评价加0.5
        vol = models.Volunteering.objects.filter(id='001').first()
        user = models.User.objects.filter(sid=info.get('user_id')).first()
        models.Comment.objects.create(tag=info.get('tag_id'),content=info.get('content'),user_id=str(user.sid),volunteering_id=int(info.get('vol_id')))
        models.User_to_Don.objects.create(user_id=str(user.sid),donated_time_id='1')
        user.total_don_time += 0.5
        user.save()
    return HttpResponse(json.dumps(response))

def add_apply(request):
    response = {'code': 0, 'msg': 'success'}
    if request.method == 'POST':
        body = str(request.body, encoding='utf-8')
        print(body)
        try:
            info = json.loads(body)  # 解析json报文
        except:
            response['code'] = '-1'
            response['msg'] = '请求格式有误'
        # content\user\volunteering
        info['volunteering_id'] = int(info.get('volunteering_id'))
        models.Apply.objects.create(**info)
    return HttpResponse(json.dumps(response))

def post_pic(request):
    response = {'code': 0, 'msg': 'success'}
    if request.method == 'POST':
        body = str(request.body, encoding='utf-8')
        try:
            info = json.loads(body)  # 解析json报文
        except:
            response['code'] = '-1'
            response['msg'] = '请求格式有误'
        file_obj = info.get('imgs')
        img_dic = {}
        x = 1
        for f in file_obj:
            index = 'picture' + str(x)
            x += 1
            img_dic[index] = f
        vol = models.Volunteering.objects.filter(id=int(info.get('id')))
        user_list_real = info.get('id_name')
        x = 0
        for i in user_list_real:
            user_list_real[x] = i.split()[0]
            x += 1
        real_end_time = (datetime.datetime.now() + relativedelta(hours=8)).strftime("%Y-%m-%d %H:%M:%S")
        vol.update(**img_dic,state=2,real_end_time = real_end_time)
        # 加时长
        user_list = models.User.objects.filter(participate_vol=vol)
        don = models.Donated_time.objects.filter(volunteering=vol).first()
        time = vol.first().time
        for u in user_list:
            if u.sid in user_list_real:
                u.total_don_time += time
                models.User_to_Don.objects.create(user=u, donated_time=don)
            else:
                models.User_to_Don.objects.create(user=u, donated_time_id=2)
            u.save()
    return HttpResponse(json.dumps(response))

def add_avatar(request):
    response = {'code': 0, 'msg': 'success'}
    if request.method == 'POST':
        body = str(request.body, encoding='utf-8')
        try:
            info = json.loads(body)  # 解析json报文
        except:
            response['code'] = '-1'
            response['msg'] = '请求格式有误'
        models.User.objects.filter(sid=info.get('sid')).update(avatar=info.get('avatar'))
    return HttpResponse(json.dumps(response))

def data(request):
    response={'code':0,'msg':'success'}
    data = {'vol_top3':{},'top5':{},'statistics':{},'annual':{},'user_top3':{}}
    if request.method=='GET':
        # ========================vol_top3====================
        v_list = models.Volunteering.objects.exclude(creater_id="18996343508").annotate(count_num=Count('apply__id')).values_list('name','count_num').order_by('-count_num')[:3]
        data['vol_top3']['vol_top1'] = {}
        data['vol_top3']['vol_top2'] = {}
        data['vol_top3']['vol_top3'] = {}
        data['vol_top3']['vol_top1']['name'] = v_list[0][0]
        data['vol_top3']['vol_top1']['count'] = v_list[0][1]
        data['vol_top3']['vol_top2']['name'] = v_list[1][0]
        data['vol_top3']['vol_top2']['count'] = v_list[1][1]
        data['vol_top3']['vol_top3']['name'] = v_list[2][0]
        data['vol_top3']['vol_top3']['count'] = v_list[2][1]
        # =========================top5========================
        cc_list = models.College.objects.all().annotate(count_num = Count('user__sid')).values_list('name','count_num').exclude(user__is_society=1,count_num = 0).order_by('-count_num')
        c_list = {}
        for c in cc_list:
            u_list = models.User.objects.filter(college__name=c[0])
            count = c[1]
            for u in u_list:
                if not models.User_to_Vol.objects.filter(user=u):
                    count -= 1
            if c[1] != 0 :
                c_list[c[0]] = count
        c_list = sorted(c_list.items(), key=lambda x: x[1],reverse=True)[:5]
        x = 0
        for i in c_list:
            data['top5'][str(x)] = i
            x += 1
        # =====================statistics====================================
        data['statistics']['stu'] = models.User.objects.filter(is_society=0).count()
        data['statistics']['soc'] = models.User.objects.filter(is_society=1).count()
        data['statistics']['ing'] = models.Volunteering.objects.filter(state=1).count()
        data['statistics']['ed'] = models.Volunteering.objects.filter(state=2).count()
        data['statistics']['yet'] = models.Volunteering.objects.filter(state=0).count()
        # ===========================annual===============================
        date = datetime.datetime.now()
        month = int(date.strftime("%m"))
        month_num = 0
        while month != month_num:
            need_time = date - relativedelta(months=month_num)
            need_time1 = date - relativedelta(months=month_num+1)
            count = models.Volunteering.objects.filter(start_time__lte=need_time).filter(start_time__gt=need_time1).count()
            data['annual'][str(month - month_num)] = count
            month_num += 1
        # ===========================user_top3============================
        u_list = models.User.objects.all().order_by('-total_don_time')[:3]
        index = 'user_top'
        index_num = 1
        for u in u_list:
            index = index + str(index_num)
            data['user_top3'][index] = {}
            data['user_top3'][index]['name'] = u.name
            data['user_top3'][index]['count'] = u.total_don_time
            index_num += 1
        response['data'] = data
    return HttpResponse(json.dumps(response))

















