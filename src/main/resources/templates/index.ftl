<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>FreeMarker Test</title>
</head>
<body>
    Hello ${name}

    <#--biubiubiu-->

    <hr>

    <h3>For each list</h3>

    <table cellpadding="0" cellspacing="0">

        <tr>
            <td>name</td>
            <td>age</td>
            <td>birthday</td>
            <td>money</td>
        </tr>

        <#list list as stu>
            <tr>
                <td>${stu.name}</td>
                <td>${stu.age}</td>
                 <td>${stu.birthday?date}</td>
                <td>${stu.money}</td>
            </tr>
        </#list>
    </table>

    <hr>
<h3>For each Map</h3>
<mark>Method one:</mark><br>
<p>The name of  Student 1:<span>${stumap['stu1'].name}</span></p>
<p>The money of  Student 2:<span>${stumap['stu2'].money}</span></p>
    <mark>Method two:</mark><br>
<p>The age of  Student 2:<span>${stumap.stu2.age}</span></p>

<mark>Method three</mark><br>
    <#list  stumap?keys as object>
        ${stumap[object].name}
    </#list>


<p style="color: red">
    FreeMarker support logic cal: <br>
    > --------- gt <br>
    >= --------gte <br>

    < ----------lt <br>
    <= ---------lte <br>
</p>



</body>
</html>