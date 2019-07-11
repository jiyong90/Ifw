<nav class="navbar navbar-expand-lg navbar-light">
    <!-- collapse button -->
    <!-- <button type="button" id="sidebarCollapse" class="btn btn-info">
        <i class="fas fa-bars"></i>
    </button> -->

    <h1 class="logo">
        <a href="#">
            <img src="${rc.getContextPath()}/soldev/img/gnb_logo.png" alt="로고">
        </a>
    </h1>
    <div class="pr-3 d-lg-none">
        <button class="btn btn-dark d-inline-block d-lg-none ml-auto" type="button" data-toggle="collapse"
            data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
            aria-label="Toggle navigation">
            <i class="fas fa-align-justify"></i>
        </button>
    </div>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="nav navbar-nav ml-auto nav-top">
            <li class="nav-item">
                <a class="" href="#"><i class="fas fa-power-off"></i></a>
            </li>
            <li class="nav-item">
                <a class="" href="#"><i class="fas fa-power-off"></i></a>
            </li>
            <li class="nav-item">
                 <a class="nav-link" href="#" onClick="location.href='${rc.getContextPath()}/logout/${tsId}';"><span class="icon-wrap mr-sm-2"><i class="fas fa-power-off"></i></span><span>로그아웃</span></a>
            </li>
            <!-- <li class="nav-item">
                <a class="nav-link" href="#"><i class="fas fa-power-off"></i></a>
            </li> -->
        </ul>
    </div>
</nav>