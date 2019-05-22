import re
'''
@author whq
@ver   1.1v
注：谷歌学术or百度学术的标准：GB/T 7714 作者部分会严格按照三个人及以上写et.al, 因此无需修改
只有知网导出的中文文献的作者部分需要修改 ps：建议使用知网导出，因为有更详细的页码

python版本说明：大部分使用正则表达式，比较简洁也方便代码修改
               基于中文文献分为【D】和【J】，英文文献基于【C】和【J】


注意：本项目只适用从谷歌学术、百度学术、知网等引用文献。

中文论文[D]和英文期刊[J]格式基本相同。
[D]中要添加“: [硕士/博士学位论文]. ”，所有年份后面要有标点。默认[硕士学位论文]

所有标点符号为英文格式，且每个标点后有一个空格。
 
针对会议[C]引用，需要添加会议地点，时间以及出版社(需手动)。
'''

ChineseDegree = r'[\u4e00-\u9fa5](.*)\[D\]'#中文论文

ChineseJournal_Year = r'.*[\u4e00-\u9fa5](.*)\[J\](.*)\d{4},(.*)'#带年份的中文期刊

ChineseJournal_NoYear = r'.*[\u4e00-\u9fa5](.*)\[J\]'# 不带年份的中文期刊

EnglishJournal = r'.*[\w]\[J\]'#英文期刊

EnglishConf = r'.*[\w]\[C\]'#英文会议

try:
    print("请输入要读的txt文件路径和名称：（格式示例：path/paper.txt）")
    fin = open(input(), 'r+')
    print("请输入要写入的txt文件路径和名称：")
    fout= open(input(),'w+')
except FileNotFoundError:
    pass
    msg="抱歉，路径文件不存在！"
    print(msg)

for line in fin.readlines():
    if re.match(ChineseDegree, line):
        # print("是学位论文")
        reg = re.compile('^(?P<author>[^ ]*)\. (?P<title>[^ ]*)\[D\]\.(?P<school>[^ ]*),(?P<date>[^ ]*)\.')
        linebits = reg.match(line).groupdict()
        print(linebits)
        #防止split空指针异常
        if re.match(r'.*,',linebits.get('author')):
            str=linebits.get('author').split(',')
            if len(str)>3:
                linebits.update({'author': str[0]+', '+str[1]+', '+str[2]})
        fout.write(linebits.get('author')+'. '+linebits.get('title')+': [硕士学位论文]. '+linebits.get('school')+', '+linebits.get('date')+'.\n')

    # print("是中文期刊")有两种格式：一种是年份单独，有的没有年份
    elif re.match(ChineseJournal_Year, line):
        reg = re.compile('^(?P<author>.*)\.(?P<title>.*)\[J\]\.(?P<Journal>.*),(?P<year>.*),(?P<date_pages>.*)\.')
        linebits = reg.match(line).groupdict()
        print(linebits)
        # 超过三人加上'等'字符，防止split空指针异常
        if re.match(r'.*,', linebits.get('author')):
            str = linebits.get('author').split(',')
            if len(str) > 3:
                linebits.update({'author': str[0] + ', ' + str[1] + ', ' + str[2]+ '等'})

        date_pages_array=linebits.get('date_pages').split(':')
        new_date_pages=date_pages_array[0]+': '+date_pages_array[1]
        print(new_date_pages)
        #修改页码-为~，替换并更新字典
        linebits.update({'date_pages':new_date_pages.replace('-','~').split('+')[0]})
        fout.write(linebits.get('author')+'. '+linebits.get('title')+'. '+linebits.get('Journal')+', '+linebits.get('year')+', '+linebits.get('date_pages')+'.\n')

    elif re.match(ChineseJournal_NoYear, line):
        reg = re.compile('^(?P<author>.*)\.(?P<title>.*)\[J\]\.(?P<Journal>.*),(?P<date_pages>.*)\.')
        linebits = reg.match(line).groupdict()
        print(linebits)
        # 超过三人加上'等'字符，防止split空指针异常
        if re.match(r'.*,', linebits.get('author')):
            str = linebits.get('author').split(',')
            if len(str) > 3:
                linebits.update({'author': str[0] + ', ' + str[1] + ', ' + str[2]+ '等'})

        date_pages_array=linebits.get('date_pages').split(':')
        new_date_pages=date_pages_array[0]+': '+date_pages_array[1]
        print(new_date_pages)
        #修改页码-为~，替换并更新字典
        linebits.update({'date_pages':new_date_pages.replace('-','~')})
        fout.write(linebits.get('author')+'. '+linebits.get('title')+'. '+linebits.get('Journal')+', '+linebits.get('date_pages')+'.\n')
    elif re.match(EnglishConf, line):
        # print("是英文会议")
        reg = re.compile('^(?P<author>.*)\. (?P<title>.*)\[C\]//(?P<conference>.*)\. (?P<date_pages>.*)\.')
        linebits = reg.match(line).groupdict()
        print(linebits)
        # 修改页码-为~，替换并更新字典
        linebits.update({'date_pages': linebits.get('date_pages').replace('-', '~')})
        fout.write(linebits.get('author') + '. ' + linebits.get('title') + '. in: ' + linebits.get(
            'conference') + '. ' + linebits.get('date_pages') + '.\n')
    elif re.match(EnglishJournal, line):
        # print("是英文期刊")
        reg = re.compile('^(?P<author>.*)\. (?P<title>.*)\[J\]\. (?P<Journal>.*), (?P<date_pages>.*)\.')
        linebits = reg.match(line).groupdict()
        print(linebits)
        # TODO: 人名三个数等
        # 修改页码-为~，替换并更新字典
        linebits.update({'date_pages': linebits.get('date_pages').replace('-', '~')})
        # 遍历字典中的k,v
        # for k, v in linebits.items():
        #     print(k + ": " + v)
        fout.write(linebits.get('author') + '. ' + linebits.get('title') + '. ' + linebits.get(
            'Journal') + ', ' + linebits.get('date_pages') + '.\n')

